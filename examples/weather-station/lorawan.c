#include "lorawan.h"

#include "fmt.h"
#include "periph/rtc.h"
#include "net/loramac.h"
#include "semtech_loramac.h"
#include "cayenne_lpp.h"
/* +++++++ LoraWan functions +++++++ */


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
    _print_buffer(lpp.buffer, lpp.cursor, "Result\n");
    printf("SEND DATA\n");
    /* The send call blocks until done */
    /* Define how big the payload is by changing the last number */
    semtech_loramac_send(&loramac,(uint8_t *) &lpp, 16);
    /* Wait until the send cycle has completed */
    semtech_loramac_recv(&loramac);
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