#include "weather.h"

int measure_wind_direction(int wind_adc) {
    return wind_adc / 8;
}


int measure_wind_speed(int wind_adc) {
  return wind_adc;
}


int measure_rainfall(int counter) {
    return counter * 2.794;
}
