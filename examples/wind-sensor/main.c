/*
Modern Device Wind Sensor Sketch for Rev C Wind Sensor

Hardware Setup:
Wind Sensor Signals    Arduino
GND                    GND
+V                     5V
RV                     A1    // modify the definitions below to use other pins
TMP                    A0    // modify the definitions below to use other pins

Hardware setup:
Wind Sensor is powered from a regulated five volt source.
RV pin and TMP pin are connected to analog innputs.
*/


#include <math.h>
#include <stdio.h>

#include "xtimer.h"
#include "timex.h"
#include "periph/adc.h"


#define RESOLUTION      ADC_RES_10BIT // mapping to 1024 bits resolution
#define DELAY           (200LU * US_PER_MS) // 200 ms
#define RV_PIN          1 // wind sensor output
#define TMP_PIN         0 // temp sensort output

/*
CALIBRATION
to calibrate your sensor, put a glass over it, but the sensor should not be
touching the desktop surface however.
adjust the zero_wind_adjustment until your sensor reads about zero with the glass over it.
negative numbers yield smaller wind speeds and vice versa.
*/
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



int main(void)
{
    xtimer_ticks32_t last = xtimer_now();

    int wind_adc;
    int temp_adc;
    int wind_speed;

    puts("\nTINIA Prototype Wind Sensor Test\n");
    puts("This test will sample the ADC RV pin of a wind sensor with\n"
         "a 10-bit resolution and print the sampled results to STDIO\n\n");

    /* initialize wind output */
      if (adc_init(ADC_LINE(RV_PIN)) < 0) {
          puts("Initialization of wind sensor pin failed\n");
          return 1;
      } else {
          puts("Successfully initialized wind sensor pin\n");
      }

      /* initialize temp output */
      if (adc_init(ADC_LINE(TMP_PIN)) < 0) {
          puts("Initialization of temp sensor pin failed\n");
          return 1;
      } else {
          puts("Successfully initialized temp sensor pin\n");
      }

    while (1) {
      // sample initialized sensors
      wind_adc = adc_sample(ADC_LINE(RV_PIN), RESOLUTION);
      temp_adc = adc_sample(ADC_LINE(TMP_PIN), RESOLUTION);

      wind_speed = measure_wind_speed(wind_adc, temp_adc);

      printf("%i\n", wind_speed);
      xtimer_periodic_wakeup(&last, DELAY);
    }

    return 0;

}
