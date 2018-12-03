#include <math.h>
#include <stdio.h>

#include "xtimer.h"
#include "timex.h"
#include "periph/adc.h"


#define RESOLUTION      ADC_RES_6BIT // mapping to 1024 bits resolution
#define DELAY           (200LU * US_PER_MS) // 200 ms
#define WND_STR         2



int main(void)
{
    xtimer_ticks32_t last = xtimer_now();

    int adc;

    puts("\nTINIA Prototype Wind Sensor Test\n");
    puts("This test will sample the ADC RV pin of a wind sensor with\n"
         "a 10-bit resolution and print the sampled results to STDIO\n\n");

    /* initialize wind output */
      if (adc_init(ADC_LINE(WND_STR)) < 0) {
          return 1;
      }

    while (1) {
      // sample initialized sensors
      adc = adc_sample(ADC_LINE(WND_STR), RESOLUTION);
      printf("Wind Strength: %i\n", adc);
      xtimer_periodic_wakeup(&last, DELAY);
    }

    return 0;

}
