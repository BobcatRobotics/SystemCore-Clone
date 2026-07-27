# SystemCore Clone

A simple Raspberry Pi–based project for running **CAN** or **CAN FD** interfaces.


## Required Software
The following software is required to create, flash, and use the SystemCore image.

### Raspberry Pi Imager
Used to flash the operating system image to a microSD card.
- https://www.raspberrypi.com/software/

### WPILib 2027.0.0-alpha-6
Required if you are using the latest WPILib beta release.
- https://github.com/wpilibsuite/allwpilib/releases/tag/v2027.0.0-alpha-6

### SystemCore Base Image
This is the base os image used if you are preperaing or setting up your own image instead of using Bobcat's Prebuilt image
- https://github.com/LimelightVision/systemcore-os-public/releases


## Required Hardware
The following hardware that we have tested with 

### Raspberry Pi 5 4 GB
A standard Raspberry Pi 5 with 4GB of memory is as close to the specs as you will get.
- https://www.amazon.com/dp/B0FL21867J?ref=ppx_yo2ov_dt_b_fed_asin_title&th=1

### CAN HAT - NO FD
This is the exact hat that was purchased from amazon for this project that does not support FD. 
- https://www.amazon.com/dp/B087RJ6XGG?ref=ppx_yo2ov_dt_b_fed_asin_title

### CAN HAT - FD
This is the exact hat that was purchased from amazon for this project that supports CAN FD. 
- https://www.amazon.com/dp/B07YQTMQTR?ref=ppx_yo2ov_dt_b_fed_asin_title

## Documentation & Guides

#### CAN HAT Setup
Follow the instructions for configuring the standard CAN HAT.

- [CAN HAT Instructions](./no-fd/README.md)

#### CAN FD HAT Setup
Follow the instructions for configuring the CAN FD HAT.

- [CAN FD HAT Instructions](./fd/README.md)

#### SD Card Setup & Cloning
Instructions for preparing a new SD card or cloning an existing SystemCore installation.

- [Setup & Build Guide](./setup/README.md)

