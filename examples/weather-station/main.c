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

static kernel_pid_t datahandler_pid;
static kernel_pid_t winddir_pid;
static char data_handler_stack[THREAD_STACKSIZE_DEFAULT];
static char wind_dir_stack[THREAD_STACKSIZE_DEFAULT];

int last_timestamp;

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

    int windspeed;
    int rainfall;
    int winddir;

    msg_t msg;
    
    while (1) {
        msg_receive(&msg);
        if (msg.type == WINDSPEED_TYPE)
        {            
            windspeed = measure_wind_speed(++wind_counter);
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


int main(void) {
    
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
    if (gpio_init_int(GPIO_PIN(GPIO_PORT, RAINFALL_PIN), GPIO_IN_PD, GPIO_RISING, rain_callback, (void *) &last_timestamp) < 0) {
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

    
    puts("==============================\n");
    puts("====== Welcome to TINIA ======\n");
    puts("==============================\n");

    // shell
    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(NULL, line_buf, SHELL_DEFAULT_BUFSIZE);

    return 0;
}
