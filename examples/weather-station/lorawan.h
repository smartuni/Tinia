
#define SENDER_PRIO         (THREAD_PRIORITY_MAIN - 1)

/* Messages are sent every 20s to respect the duty cycle on each channel */
#define PERIOD              (20U)


static void rtc_cb(void *arg);
static void _prepare_next_alarm(void);
static void _send_message(void);
static void *sender(void *arg);