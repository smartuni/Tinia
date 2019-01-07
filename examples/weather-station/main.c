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
// static kernel_pid_t winddir_pid;
static char stack[THREAD_STACKSIZE_MAIN];

int last_timestamp;

/**
 *  Sends a message to the data handler if the window between the current 
 *  and last call is longer than a specified timeout.
 */
static void rain_callback(void *arg) {
    (void) arg;
    
    msg_t msg;
    msg_t msg_queue[8];
    msg_init_queue(msg_queue, 8);

    int current_timestamp = xtimer_now_usec();
    int timediff = current_timestamp - last_timestamp;
    // only if current call is out of the time window
    if (timediff > TIMEOUT) {
        printf("TRIGGER_RAINFALL: %i\n", timediff);
        msg.type = RAINFALL_TYPE;
        msg_send(&msg, datahandler_pid);
    }

    last_timestamp = current_timestamp;

}

/**
 *  Sends a message to the data handler every time the function was called. 
 */
// static void windspeed_callback(void *arg) {
//     DEBUG("TRIGGERED WINDSPEED CALLBACK");
    
//     (void) arg;

//     msg_t msg;
//     msg_t msg_queue[8];
//     msg_init_queue(msg_queue, 8);

//     int content = WINDSPEED_TYPE;
//     msg.content.ptr = &content;
    
//     msg_send(&msg, winddir_pid);
//     msg_send(&msg, datahandler_pid);
// }

/**
 *  Handles incoming data messages and calculates outgoing weather data.
 */
static void *data_handler(void *arg) {
    (void) arg;

    int rain_counter = 0;
    int wind_counter = 0;

    int windspeed;
    // int rainfall;
    int winddir;

    msg_t msg;
    msg_t msg_queue[8];
    msg_init_queue(msg_queue, 8);

    puts("Initialized data handler queue.");
    
    while (1) {
        msg_receive(&msg);
        printf("Recieved message: %i\n", msg.type);
        if (msg.type == WINDSPEED_TYPE)
        {            
            windspeed = measure_wind_speed(++wind_counter);
            printf("SPD: %i\n", windspeed);
        }
        if (msg.type == RAINFALL_TYPE)
        {
            measure_rainfall(++rain_counter);
            printf("RNF: %i\n", rain_counter);
        }
        if (msg.type == WINDDIR_TYPE)
        {
            // winddir = measure_wind_direction(msg);
            winddir = 0;
            printf("DIR: %i\n", winddir);
        }
    }

    return NULL;
}

/**
 *  Handles sending data for the wind direction.
 */
// static void *winddir_handler(void *arg) {
//     (void) arg;
//     int wind_direction;
//     int adc_value;
    
//     msg_t msg;
//     msg_t msg_queue[8];
//     msg_init_queue(msg_queue, 8);

//     /* initialize wind output */
//     if (adc_init(ADC_LINE(WINDDIR_PIN)) < 0) {
//         puts("Initialization of wind sensor pin failed\n");
//     } else {
//         puts("Successfully initialized wind sensor pin\n");
//     }
//     DEBUG("wind direction initialized\n");


    
//     while (1) {
//         msg_receive(&msg);
//         // if wind speed is > least wind speed needed
//                 adc_value = adc_sample(ADC_LINE(WINDDIR_PIN), RESOLUTION);
//                 wind_direction = measure_wind_direction(adc_value);
//                 msg.content.ptr = &wind_direction;
//                 msg_send(&msg, datahandler_pid);
//     }

//     return NULL;
// }


int main(void) {
    
    // data handler thread
    puts("Starting TINIA main application thread ...");
    datahandler_pid = thread_create(stack, sizeof(stack),
                        THREAD_PRIORITY_MAIN - 2,
                        0,
                        data_handler,
                        NULL, "data handler thread");
    puts("complete\n");


    // data sensor threads
    puts("Initializing sensors...\n");

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
    // puts("windspeed");
    // if (gpio_init_int(GPIO_PIN(GPIO_PORT, WINDSPEED_PIN), 2, 0, windspeed_callback, (void *) &last_timestamp) < 0) {
    //     printf("error: init_int of GPIO_PIN(%i, %i) failed\n", GPIO_PORT, WINDSPEED_PIN);
    //     return 1;
    // }
    // printf("GPIO_PIN(%i, %i) successfully initialized as ext int\n", GPIO_PORT, WINDSPEED_PIN);

    

    // // wind direction sensor
    // puts("winddirection");
    // // weather direction thread
    // winddir_pid = thread_create(stack, sizeof(stack),
    //                     THREAD_PRIORITY_MAIN - 1,
    //                     0,
    //                     winddir_handler,
    //                     NULL, "wind direction thead");
    // puts("complete\n");


    puts("Initialized weather sensors.");

    
    puts("==============================\n");
    puts("====== Welcome to TINIA ======\n");
    puts("==============================\n");

    // shell
    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(NULL, line_buf, SHELL_DEFAULT_BUFSIZE);

    return 0;
}
