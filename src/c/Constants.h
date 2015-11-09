#ifndef __CONSTANTS_H__
#define __CONSTANTS_H__

#define null 0

#define G13_INTERFACE 0
#define G13_KEY_ENDPOINT 1
#define G13_LCD_ENDPOINT 2
#define G13_KEY_READ_TIMEOUT 0
#define G13_VENDOR_ID 0x046d
#define G13_PRODUCT_ID 0xc21c
#define G13_REPORT_SIZE 8
#define G13_LCD_BUFFER_SIZE 0x3c0
#define G13_NUM_KEYS 40

enum stick_mode_t { STICK_KEYS = 0, STICK_ABSOLUTE, /*STICK_RELATIVE,*/  };

enum stick_key_t { STICK_LEFT, STICK_UP, STICK_DOWN, STICK_RIGHT };

enum G13_KEYS {
    /* byte 3 */
    G13_KEY_G1 = 0,
    G13_KEY_G2,
    G13_KEY_G3,
    G13_KEY_G4,

    G13_KEY_G5,
    G13_KEY_G6,
    G13_KEY_G7,
    G13_KEY_G8,

    /* byte 4 */
    G13_KEY_G9,
    G13_KEY_G10,
    G13_KEY_G11,
    G13_KEY_G12,

    G13_KEY_G13,
    G13_KEY_G14,
    G13_KEY_G15,
    G13_KEY_G16,

    /* byte 5 */
    G13_KEY_G17,
    G13_KEY_G18,
    G13_KEY_G19,
    G13_KEY_G20,

    G13_KEY_G21,
    G13_KEY_G22,
    G13_KEY_UNDEF1,
    G13_KEY_LIGHT_STATE,

    /* byte 6 */
    G13_KEY_BD,
    G13_KEY_L1,
    G13_KEY_L2,
    G13_KEY_L3,

    G13_KEY_L4,
    G13_KEY_M1,
    G13_KEY_M2,
    G13_KEY_M3,

    /* byte 7 */
    G13_KEY_MR,
    G13_KEY_LEFT,
    G13_KEY_DOWN,
    G13_KEY_TOP,

    G13_KEY_UNDEF3,
    G13_KEY_LIGHT,
    G13_KEY_LIGHT2,
    G13_KEY_MISC_TOGGLE
};

#define G13_KEY_ONLY_MASK  (_BV(G13_G1)  | \
                            _BV(G13_G2)  | \
                            _BV(G13_G3)  | \
                            _BV(G13_G4)  | \
                            _BV(G13_G5)  | \
                            _BV(G13_G6)  | \
                            _BV(G13_G7)  | \
                            _BV(G13_G8)  | \
                            _BV(G13_G9)  | \
                            _BV(G13_G10) | \
                            _BV(G13_G11) | \
                            _BV(G13_G12) | \
                            _BV(G13_G13) | \
                            _BV(G13_G14) | \
                            _BV(G13_G15) | \
                            _BV(G13_G16) | \
                            _BV(G13_G17) | \
                            _BV(G13_G18) | \
                            _BV(G13_G19) | \
                            _BV(G13_G20) | \
                            _BV(G13_G21) | \
                            _BV(G13_G22) | \
                            _BV(G13_BD)  | \
                            _BV(G13_L1)  | \
                            _BV(G13_L2)  | \
                            _BV(G13_L3)  | \
                            _BV(G13_L4)  | \
                            _BV(G13_M1)  | \
                            _BV(G13_M2)  | \
                            _BV(G13_M3)  | \
                            _BV(G13_MR)  | \
                            _BV(G13_LIGHT))


#endif
