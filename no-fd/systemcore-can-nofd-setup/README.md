# DIY SystemCore CAN setup

Turns an **official FIRST SystemCore image** running on a Raspberry Pi 5 into a working
DIY substitute that drives CTRE Phoenix 6 devices over **on-board MCP2515 CAN pins**
and/or a **CANivore** — with everything coming up automatically at boot.

This is a small **overlay installer**, not a full OS image (see *Why no full image?* below).

## What it does

On every boot, a oneshot service (`can-bringup.service` → `diy-can-setup.sh`):

1. Detects the two MCP2515 SPI CAN controllers and names them by **stable SPI path**
   (probe-order independent), sets **1 Mbit**, and brings them up:
   - `spi0.1` → `can_s0`  (addressed in code as `CANBus.systemcore(0)`)
   - `spi0.0` → `can_s1`  (`CANBus.systemcore(1)`)
2. Creates `can_s2`/`can_s3`/`can_s4` as **virtual (vcan)** dummies — the WPILib HAL aborts
   at startup unless all of `can_s0`–`can_s4` exist.
3. Loads the `robot_heartbeat` kernel module **after** the CAN interfaces exist (it fails
   with *"No such device"* if loaded too early), which creates the `/dev/mrccan/*` enable
   interface the Phoenix native and HAL need.

It also removes `robot_heartbeat` from `/etc/modules-load.d/` (where the stock image loads
it too early).

### With or without the pin board

The script auto-detects per bus:

| Hardware | `can_s0` / `can_s1` | `can_s2`–`s4` | Drive devices via |
| --- | --- | --- | --- |
| MCP2515 pin board fitted | physical @ 1 Mbit | vcan | `CANBus.systemcore(0/1)` |
| No pin board | vcan | vcan | CANivore: `new CANBus("reefmaster")` |

Either way the robot boots fully and the enable interface comes up.

**Force all buses virtual** even with the board fitted (CANivore-only):
```bash
sudo touch /etc/diy-can-virtual-only   # then reboot;  rm to go back to physical
```

## Requirements

- A Raspberry Pi 5 flashed with the **official FIRST SystemCore image** (obtain it
  yourself from FIRST/CTRE — it is not redistributed here).
- For physical CAN: **two MCP2515 CAN controllers on SPI0** (`spi0.0`, `spi0.1`), each with
  proper 120 Ω bus termination. (No board? It still works over a CANivore.)
- Phoenix 6 device firmware that **matches the Phoenix API version** in your robot project
  (mismatched versions give *"API too old / CAN frame too-stale"* — flash the device in
  Phoenix Tuner X to match).

## Install

Copy this folder to the Pi and run the installer:
```bash
scp -r systemcore-can-setup systemcore@<pi-address>:/home/systemcore/
ssh systemcore@<pi-address>
cd ~/systemcore-can-setup
sudo ./install.sh
sudo reboot
```

## Verify
```bash
ip -br link show | grep can_s          # can_s0/can_s1 up; can_s2-4 present
ls /dev/mrccan/                        # controldata controldataro enabledro matchinfo matchinforo
systemctl is-active can-bringup robot  # both active
```
Driving a motor: deploy your robot code, then **enable from the Driver Station** (Enable
button — **not** Space/Enter, which are Emergency-Stop and latch the robot disabled).

## Pin / bus reference

| Code | SocketCAN | Hardware |
| --- | --- | --- |
| `CANBus.systemcore(0)` | `can_s0` | MCP2515 on `spi0.0` |
| `CANBus.systemcore(1)` | `can_s1` | MCP2515 on `spi0.1` |
| `new CANBus("reefmaster")` | `can2` | CANivore (USB) |

(Don't know which physical connector is which? Plug in a powered device and watch which
interface's `rx_packets` climbs: `cat /sys/class/net/can_s0/statistics/rx_packets`.)

## Uninstall
```bash
sudo systemctl disable --now can-bringup.service
sudo rm /etc/systemd/system/can-bringup.service /usr/local/sbin/diy-can-setup.sh
sudo systemctl daemon-reload
```

## Why no full SD-card image?

The SystemCore base contains **proprietary** FIRST/CTRE software (`robot_heartbeat.ko`,
`MrcCommDaemon`, the Phoenix native libraries, the SystemCore HAL). Redistributing a full
`.img` would redistribute those, which isn't permitted. So the shareable artifact is this
overlay; everyone brings their own official base image.

### Making a personal full image (for your own backup)

Best done with the **card in a reader on a separate Linux machine** (not over SSH on the
live Pi — a mounted root produces an inconsistent image):

```bash
# 1) Power off the Pi, put the SD card in a Linux machine. Find the device (e.g. /dev/sdX).
sudo dd if=/dev/sdX of=systemcore-diy.img bs=4M status=progress

# 2) Shrink it so it flashes to any >= size card (PiShrink also re-expands on first boot):
#    https://github.com/Drewsif/PiShrink
sudo pishrink.sh -a systemcore-diy.img    # -a also regenerates SSH host keys on first boot

# 3) Flash with Raspberry Pi Imager / balenaEtcher to another card.
```
Before sharing **any** image, also change the default `systemcore`/`systemcore` password
and clear `~/logs`. And remember the licensing note above — keep full images for personal
use only.
