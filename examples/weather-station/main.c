#include <math.h>
#include <stdio.h>

#include "xtimer.h"
#include "timex.h"
#include "periph/adc.h"


#define RESOLUTION      ADC_RES_10BIT // mapping to 1024 bits resolution
#define DELAY           (200LU * US_PER_MS) // 200 ms
#define ADC_PIN         0



int main(void)
{
    xtimer_ticks32_t last = xtimer_now();

    int wind_adc;

    puts("\nTINIA Prototype Wind Sensor Test\n");
    puts("This test will sample the ADC RV pin of a wind sensor with\n"
         "a 10-bit resolution and print the sampled results to STDIO\n\n");

    /* initialize wind output */
      if (adc_init(ADC_LINE(ADC_PIN)) < 0) {
          puts("Initialization of wind sensor pin failed\n");
          return 1;
      } else {
          puts("Successfully initialized wind sensor pin\n");
      }

    while (1) {
      // sample initialized sensors
      wind_adc = adc_sample(ADC_LINE(ADC_PIN), RESOLUTION);
      printf("%i\n", wind_adc);
      xtimer_periodic_wakeup(&last, DELAY);
    }

    return 0;

}
