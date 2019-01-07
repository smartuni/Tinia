#include "weather.h"

int measure_wind_direction(int wind_adc) {
  	int wind_direction = -1;

  	if (wind_adc >= 57) {
  		wind_direction = WEST;
  	}
  	else if (wind_adc >= 54) {
  		wind_direction = NORTHWEST;
  	}
  	else if (wind_adc >= 48) {
  		wind_direction = NORTH;
  	}
  	else if (wind_adc >= 38) {
  		wind_direction = SOUTHWEST;
  	}
  	else if (wind_adc >= 28) {
  		wind_direction = NORTHEAST;
  	}
  	else if (wind_adc >= 17) {
  		wind_direction = SOUTH;
  	}
  	else if (wind_adc >= 11) {
  		wind_direction = SOUTHEAST;
  	}
  	else if (wind_adc >= 5) {
  		wind_direction = EAST;
  	}


  	return wind_direction;
}


int measure_wind_speed(int wind_counter) {
  return windspeed;
}


int measure_rainfall(int counter) {
    return counter * 2794;
}
