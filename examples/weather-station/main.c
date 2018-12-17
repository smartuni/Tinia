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

typedef enum TYPES {
    WINDSPEED_TYPE,
    RAINFALL_TYPE,
    WINDDIR_TYPE
} TYPES;

typedef enum PINS {
    WINDSPEED_PIN,
    WINDDIR_PIN,
    RAINFALL_PIN
} PINS;

typedef enum WIND_DIRECTION {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST
} WIND_DIRECTION;

#define GPIO_PORT       0
#define TIMEOUT     10000
#define RESOLUTION ADC_RES_6BIT

static kernel_pid_t datahandler_pid;
static kernel_pid_t winddir_pid;
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

    // only if current call is out of the time window
    if ((current_timestamp - last_timestamp) > TIMEOUT) {
        int content = RAINFALL_TYPE;
        msg.content.ptr = &content;
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
    msg_t msg_queue[8];
    msg_init_queue(msg_queue, 8);
    int content = WINDSPEED_TYPE;
    msg.content.ptr = &content;
    msg_send(&msg, winddir_pid);
    msg_send(&msg, datahandler_pid);
}

/**
 *  Handles incoming data messages and calculates outgoing weather data.
 */
static void *data_handler(void *arg) {
    (void) arg;

    int windspeed;
    int rainfall;
    int winddir;

    msg_t msg;
    msg_t msg_queue[8];
    msg_init_queue(msg_queue, 8);
    
    while (1) {
        msg_receive(&msg);
        // if message sender is windspeed
            // calculate new wind speed
            windspeed = 0;
            printf("SPD: %i\n", windspeed);
        // if message sender is rainfall
            // calculate new rainfall
            rainfall = 0;
            printf("RNF: %i\n", rainfall);
        // if message sender is winddir
            // calculate new winddir
            winddir = NORTH;
            printf("DIR: %i\n", winddir);
    }

    return NULL;
}

int measure_wind_direction(int wind_adc) {
    if (wind_adc < 32) {
        return NORTH; 
    } else {
        return SOUTH;
    }
}


/**
 *  Handles sending data for the wind direction.
 */
static void *winddir_handler(void *arg) {
    (void) arg;
    int wind_direction;
    int adc_value;
    
    msg_t msg;
    msg_t msg_queue[8];
    msg_init_queue(msg_queue, 8);

    /* initialize wind output */
    if (adc_init(ADC_LINE(WINDDIR_PIN)) < 0) {
        puts("Initialization of wind sensor pin failed\n");
    } else {
        puts("Successfully initialized wind sensor pin\n");
    }


    
    while (1) {
        msg_receive(&msg);
        // if wind speed is > least wind speed needed
                adc_value = adc_sample(ADC_LINE(WINDDIR_PIN), RESOLUTION);
                wind_direction = measure_wind_direction(adc_value);
                msg.content.ptr = &wind_direction;
                msg_send(&msg, datahandler_pid);
    }

    return NULL;
}


int main(void) {
    
    // data handler thread
    puts("Starting TINIA main application thread ...");
    datahandler_pid = thread_create(stack, sizeof(stack),
                        THREAD_PRIORITY_MAIN - 1,
                        0,
                        data_handler,
                        NULL, "data handler thead");
    puts("complete\n");


    // data sensor threads
    puts("Initializing sensors...\n");

    last_timestamp = xtimer_now_usec();

    

    // rainfall sensor
    puts("rainfall");
    printf("%i\n", last_timestamp);
    if (gpio_init_int(GPIO_PIN(GPIO_PORT, RAINFALL_PIN), 2, 1, rain_callback, (void *) &last_timestamp) < 0) {
        printf("error: init_int of GPIO_PIN(%i, %i) failed\n", 0, 1);
        return 1;
    }
    printf("GPIO_PIN(%i, %i) successfully initialized as ext int\n", 0, 0);

    

    // wind speed sensor
    puts("windspeed");
    if (gpio_init_int(GPIO_PIN(GPIO_PORT, WINDSPEED_PIN), 2, 0, windspeed_callback, (void *) &last_timestamp) < 0) {
        printf("error: init_int of GPIO_PIN(%i, %i) failed\n", 0, 1);
        return 1;
    }
    printf("GPIO_PIN(%i, %i) successfully initialized as ext int\n", 0, 1);

    

    // wind direction sensor
    puts("winddirection");
    // weather direction thread
    winddir_pid = thread_create(stack, sizeof(stack),
                        THREAD_PRIORITY_MAIN - 1,
                        0,
                        winddir_handler,
                        NULL, "wind direction thead");
    puts("complete\n");


    puts("Initialized weather sensors.");

    
    puts("==============================\n");
    puts("====== Welcome to TINIA ======\n");
    puts("==============================\n");

    // shell
    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(NULL, line_buf, SHELL_DEFAULT_BUFSIZE);

    return 0;
}
