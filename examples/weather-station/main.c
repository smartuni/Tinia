/*
 * Copyright (C) 2018 Inria
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 */

#include <stdio.h>
#include <string.h>

#include "irq.h"
#include "xtimer.h"
#include "thread.h"
#include "msg.h"
#include "shell.h"
#include "periph/gpio.h"

static int timeout = 10;
static kernel_pid_t pid;
static char stack[THREAD_STACKSIZE_MAIN];


static void cb(void *arg)
{
    // measure current time stamp
    int current_timestamp = xtimer_now_usec();
    // get last time stamp from arg
    int last_timestamp = (int)arg;
    // if current time stamp - last time stamp > timeout
    if ((current_timestamp - last_timestamp) > timeout) {
      // send message to handler
      msg_t msg;
      msg.content.ptr = "triggered";
      msg_send(&msg, pid);
      printf("Timedelta since last time: %i\n", current_timestamp - last_timestamp);
      printf("INT: send interrupt from pin %i at %i\n", (int)arg, current_timestamp);
      // set arg to current time stamp
      arg = &current_timestamp;
    }
}

static void *thread_handler(void *arg)
{
    (void) arg;
    float counter = 0;

    while (1) {
        msg_t msg;
        msg_receive(&msg);
        printf("Message received: %s to %i\n", (char *)msg.content.ptr, (int)pid);
        counter += 0.2794;
        printf("Rainfall Sensor Triggered: %i\n", (int)counter);
        printf("Time: %" PRIu32 "\n", xtimer_usec_from_ticks(xtimer_now()));
    }

    return NULL;
}

int main(void)
{
    puts("RIOT application with thread IPC");

    int value = 1;

    pid = thread_create(stack, sizeof(stack),
                        THREAD_PRIORITY_MAIN - 1,
                        0,
                        thread_handler,
                        NULL, "my thread");

    if (gpio_init_int(GPIO_PIN(0,1),2,1, cb, (void *)value) < 0) {
        printf("error: init_int of GPIO_PIN(%i, %i) failed\n", 0, 1);
        return 1;
    }
    printf("GPIO_PIN(%i, %i) successfully initialized as ext int\n", 0, 1);

    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(NULL, line_buf, SHELL_DEFAULT_BUFSIZE);

    return 0;
}
