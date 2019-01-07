#include <stdio.h>
#include <string.h>

#include "irq.h"
#include "xtimer.h"
#include "thread.h"
#include "msg.h"
#include "shell.h"
#include "periph/gpio.h"
#include "periph/adc.h"
#include "periph/rtc.h"
#include "weather.h"
#include "debug.h"
#include "fmt.h"
#include "net/loramac.h"
#include "semtech_loramac.h"
#include "cayenne_lpp.h"

static kernel_pid_t datahandler_pid;
static kernel_pid_t winddir_pid;
static kernel_pid_t sender_pid;
static char sender_stack[THREAD_STACKSIZE_MAIN / 2];
static char data_handler_stack[THREAD_STACKSIZE_DEFAULT];
static char wind_dir_stack[THREAD_STACKSIZE_DEFAULT];
static uint8_t deveui[LORAMAC_DEVEUI_LEN];
static uint8_t appeui[LORAMAC_APPEUI_LEN];
static uint8_t appkey[LORAMAC_APPKEY_LEN];
static cayenne_lpp_t lpp;

semtech_loramac_t loramac;


/* Messages are sent every 20s to respect the duty cycle on each channel */
#define PERIOD              (20U)

#define SENDER_PRIO         (THREAD_PRIORITY_MAIN - 1)


int last_timestamp;
int windspeed_max;
int windspeed;
int rainfall;
int winddir;

/**
 *  Sends a message to the data handler if the window between the current 
 *  and last call is longer than a specified timeout.
 */
static void rain_callback(void *arg) {
    (void) arg;
    
    msg_t msg;

    int current_timestamp = xtimer_now_usec();
    int timediff = current_timestamp - last_timestamp;
    // only if current call is out of the time window
    if (timediff > TIMEOUT) {
        msg.type = RAINFALL_TYPE;
        msg_send(&msg, datahandler_pid);
    }

    last_timestamp = current_timestamp;

}

/**
 *  Sends a message to the data handler every time the function was called. 
 */
static void windspeed_callback(void *arg) {
    (void) arg;

    msg_t msg;
    msg.type = WINDSPEED_TYPE;
    msg_send(&msg, winddir_pid);
    msg_send(&msg, datahandler_pid);
}

/**
 *  Handles incoming data messages and calculates outgoing weather data.
 */
static void *data_handler(void *arg) {
    (void) arg;

    int rain_counter = 0;
    int wind_counter = 0;


    msg_t msg;
    
    while (1) {
        msg_receive(&msg);
        if (msg.type == WINDSPEED_TYPE)
        {            
            windspeed = measure_wind_speed(++wind_counter);
            if (windspeed > windspeed_max)
                {
                    windspeed_max = windspeed;
                }
            printf("SPD: %i\n", windspeed);
        }
        if (msg.type == RAINFALL_TYPE)
        {
            rainfall = measure_rainfall(++rain_counter);
            printf("RNF: %i\n", rainfall);
        }
        if (msg.type == WINDDIR_TYPE)
        {
            winddir = measure_wind_direction(msg.content.value);
            printf("DIR: %i\n", winddir);
        }
    }

    return NULL;
}

/**
 *  Handles sending data for the wind direction.
 */
static void *winddir_handler(void *arg) {
    (void) arg;
    int adc_value;
    
    msg_t msg;

    /* initialize wind output */
    if (adc_init(ADC_LINE(WINDDIR_PIN)) < 0) {
        puts("Initialization of wind sensor pin failed\n");
    } else {
        puts("Successfully initialized wind sensor pin\n");
    }
    printf("wind direction initialized\n");


    
    while (1) {
        msg_receive(&msg);
        // TODO: if wind speed is > least wind speed needed
            adc_value = adc_sample(ADC_LINE(WINDDIR_PIN), RESOLUTION);
            msg.type = WINDDIR_TYPE;
            msg.content.value = adc_value;
            msg_send(&msg, datahandler_pid);
    }

    return NULL;
}


/* +++++++ LoraWan functions +++++++ */


static void _print_buffer(const uint8_t *buffer, size_t len, const char *msg)
{
    printf("%s: ", msg);
    for (uint8_t i = 0; i < len; i++) {
        printf("%02X", buffer[i]);
    }
}


static void rtc_cb(void *arg)
{
    (void) arg;
    msg_t msg;
    msg_send(&msg, sender_pid);
}

static void _prepare_next_alarm(void)
{
    struct tm time;
    rtc_get_time(&time);
    /* set initial alarm */
    time.tm_sec += PERIOD;
    mktime(&time);
    rtc_set_alarm(&time, rtc_cb, NULL);
}

static void _send_message(void)
{
    //printf("Sending: %d\n", message);
    //_print_buffer(lpp.buffer, lpp.cursor, "Result\n");
    printf("SEND DATA\n");
    /* The send call blocks until done */
    /* Define how big the payload is by changing the last number */
    semtech_loramac_send(&loramac,(uint8_t *) &lpp, 16);
    /* Wait until the send cycle has completed */
    //uint8_t ret;
    semtech_loramac_recv(&loramac);
    //printf("Downlink Message: %d \n", ret);

}

static void *sender(void *arg)
{
    (void)arg;

    msg_t msg;
    msg_t msg_queue[8];
    msg_init_queue(msg_queue, 8);

    while (1) {
        msg_receive(&msg);

        /* Trigger the message send */
        _send_message();

        /* Schedule the next wake-up alarm */
        _prepare_next_alarm();
    }

    /* this should never be reached */
    return NULL;
}




/***********MAIN*************/
int main(void) {

    cayenne_lpp_add_analog_output(&lpp, 1, windspeed);
    cayenne_lpp_add_analog_output(&lpp, 2, windspeed_max);
    cayenne_lpp_add_analog_output(&lpp, 3, winddir);
    cayenne_lpp_add_analog_output(&lpp, 4, rainfall);
    _print_buffer(lpp.buffer, lpp.cursor, "Result");
    
    // data handler thread
    puts("Starting TINIA main application thread ...");
    datahandler_pid = thread_create(data_handler_stack, sizeof(data_handler_stack),
                        THREAD_PRIORITY_MAIN - 1,
                        0,
                        data_handler,
                        NULL, "data handler thread");
    puts("complete\n");


    // data sensor threads
    puts("Initializing sensors...\n");

    // wind direction sensor
    puts("winddirection");
    // weather direction thread
    winddir_pid = thread_create(wind_dir_stack, sizeof(wind_dir_stack),
                        THREAD_PRIORITY_MAIN - 1,
                        0,
                        winddir_handler,
                        NULL, "wind direction thead");

    last_timestamp = xtimer_now_usec();

    // rainfall sensor
    puts("rainfall");
    printf("%i\n", last_timestamp);
    if (gpio_init_int(GPIO_PIN(1, RAINFALL_PIN), GPIO_IN_PD, GPIO_RISING, rain_callback, (void *) &last_timestamp) < 0) {
        printf("error: init_int of GPIO_PIN(%i, %i) failed\n", GPIO_PORT, RAINFALL_PIN);
        return 1;
    }
    printf("GPIO_PIN(%i, %i) successfully initialized as ext int\n", GPIO_PORT, RAINFALL_PIN);

    

    // wind speed sensor
    puts("windspeed");
    if (gpio_init_int(GPIO_PIN(GPIO_PORT, WINDSPEED_PIN), GPIO_IN_PD, GPIO_FALLING, windspeed_callback, (void *) &last_timestamp) < 0) {
        printf("error: init_int of GPIO_PIN(%i, %i) failed\n", GPIO_PORT, WINDSPEED_PIN);
        return 1;
    }
    printf("GPIO_PIN(%i, %i) successfully initialized as ext int\n", GPIO_PORT, WINDSPEED_PIN);

    puts("Initialized weather sensors.");

 
    puts("LoRaWAN Class A low-power application");
    puts("=====================================");

    /* Convert identifiers and application key */
    fmt_hex_bytes(deveui, DEVEUI);
    fmt_hex_bytes(appeui, APPEUI);
    fmt_hex_bytes(appkey, APPKEY);

    /* Initialize the loramac stack */
    semtech_loramac_init(&loramac);
    semtech_loramac_set_deveui(&loramac, deveui);
    semtech_loramac_set_appeui(&loramac, appeui);
    semtech_loramac_set_appkey(&loramac, appkey);

    /* Use a fast datarate, e.g. BW125/SF7 in EU868 */
    semtech_loramac_set_dr(&loramac, LORAMAC_DR_5);

    /* Start the Over-The-Air Activation (OTAA) procedure to retrieve the
     * generated device address and to get the network and application session
     * keys.
     */
    puts("Starting join procedure");
    if (semtech_loramac_join(&loramac, LORAMAC_JOIN_OTAA) != SEMTECH_LORAMAC_JOIN_SUCCEEDED) {
        puts("Join procedure failed");
        return 1;
    }
    puts("Join procedure succeeded"); 


    puts("==============================\n");
    puts("====== Welcome to TINIA ======\n");
    puts("==============================\n");

    /* start the sender thread */
    sender_pid = thread_create(sender_stack, sizeof(sender_stack),
                               SENDER_PRIO, 0, sender, NULL, "sender");

    /* trigger the first send */
    msg_t msg;
    msg_send(&msg, sender_pid);

    // shell
    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(NULL, line_buf, SHELL_DEFAULT_BUFSIZE);

    return 0;
}
