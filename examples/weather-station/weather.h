typedef enum TYPES {
    WINDSPEED_TYPE,
    RAINFALL_TYPE,
    WINDDIR_TYPE
} TYPES;

typedef enum PINS {
    WINDSPEED_PIN = 4,
    WINDDIR_PIN = 1,
    RAINFALL_PIN = 0
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

int measure_wind_direction(int adc_value);
int measure_wind_speed(int wind_adc);
int measure_rainfall(int counter);