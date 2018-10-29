/*
 TINIA
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <random.h>
#include <math.h>

#include "msg.h"
#include "thread.h"
#include "fmt.h"

#include "periph/adc.h"
#include "periph/rtc.h"

#include "net/loramac.h"
#include "semtech_loramac.h"
#include "cayenne_lpp.h"

/* Messages are sent every 20s to respect the duty cycle on each channel */
#define PERIOD              (5U)
#define SENDER_PRIO         (THREAD_PRIORITY_MAIN - 1)
#define RESOLUTION      ADC_RES_10BIT // mapping to 1024 bits resolution
#define RV_PIN          1 // wind sensor output
#define TMP_PIN         2 // temp sensort output

static kernel_pid_t sender_pid;
static char sender_stack[THREAD_STACKSIZE_MAIN / 2];

semtech_loramac_t loramac;

//static const char *message = "This is RIOT!";

//************************************************************************/
//cayenne cloud test buffer

static cayenne_lpp_t lpp;
const float zero_wind_adjustment =  -.46;
const float step_size = 0.0048828125;

int measure_wind_speed(int wind_adc,  int tmp_adc) {

  float wind_volts;
  float zero_wind_adc;
  float zero_wind_volt;
  float wind_speed_mph;
  float wind_speed_kmh;

  wind_volts = ((float)wind_adc *  step_size);
  zero_wind_adc = -0.0006*((float)tmp_adc * (float)tmp_adc) + 1.0727 * (float)tmp_adc + 47.172;
  zero_wind_volt = (zero_wind_adc * step_size) - zero_wind_adjustment;
  wind_speed_mph =  pow(((wind_volts - zero_wind_volt) /.2300) , 2.7265);
  wind_speed_kmh = wind_speed_mph * 1.609344; // conversion to km/h

  return (int)wind_speed_kmh;
}


static void _print_buffer(const uint8_t *buffer, size_t len, const char *msg)
{
    printf("%s: ", msg);
    for (uint8_t i = 0; i < len; i++) {
        printf("%02X", buffer[i]);
    }
}


//static uint8_t message2 = (uint8_t *) lpp;

//************************************************************************/

static uint8_t deveui[LORAMAC_DEVEUI_LEN];
static uint8_t appeui[LORAMAC_APPEUI_LEN];
static uint8_t appkey[LORAMAC_APPKEY_LEN];

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
    semtech_loramac_send(&loramac,(uint8_t *) &lpp, 4);
    /* Wait until the send cycle has completed */
    semtech_loramac_recv(&loramac);
}

static void *sender(void *arg)
{
    (void)arg;

    msg_t msg;
    msg_t msg_queue[8];
    msg_init_queue(msg_queue, 8);

    int wind_adc;
    int temp_adc;
    int wind_speed;

    /* initialize wind output */
    if (adc_init(ADC_LINE(RV_PIN)) < 0) {
        puts("Initialization of wind sensor pin failed\n");
    } else {
        puts("Successfully initialized wind sensor pin\n");
    }

    /* initialize temp output */
    if (adc_init(ADC_LINE(TMP_PIN)) < 0) {
        puts("Initialization of temp sensor pin failed\n");
    } else {
        puts("Successfully initialized temp sensor pin\n");
    }

    while (1) {

        // sample initialized sensors
        wind_adc = adc_sample(ADC_LINE(RV_PIN), RESOLUTION);
        temp_adc = adc_sample(ADC_LINE(TMP_PIN), RESOLUTION);
        wind_speed = measure_wind_speed(wind_adc, temp_adc);
        printf("%i\n", wind_speed);

        cayenne_lpp_add_analog_input(&lpp, 3, wind_speed);
        msg_receive(&msg);
        _send_message();
        cayenne_lpp_reset(&lpp);
        _prepare_next_alarm();
    }

    /* this should never be reached */
    return NULL;
}

int main(void)
{
    puts("LoRaWAN Class A low-power application");
    puts("=====================================");

    //cayenne_lpp_add_temperature(&lpp, 5, 25.5);
    //cayenne_lpp_add_temperature(&lpp, 5, 25.5);
   _print_buffer(lpp.buffer, lpp.cursor, "Result");


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

    /* start the sender thread */
    sender_pid = thread_create(sender_stack, sizeof(sender_stack),
                               SENDER_PRIO, 0, sender, NULL, "sender");

    /* trigger the first send */
    msg_t msg;
    msg_send(&msg, sender_pid);
    return 0;
}
