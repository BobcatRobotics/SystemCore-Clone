#!/bin/bash
# DIY SystemCore CAN bring-up -- works WITH or WITHOUT the Waveshare 2-CH CAN FD HAT.
#
# For each SystemCore bus can_s0..can_s4:
#   * if its physical SPI CAN controller is present (and not forced virtual), name it by
#     stable SPI path, set 1 Mbit, bring it up.   spi0.0 -> can_s0, spi1.0 -> can_s1.
#   * otherwise create it as a virtual (vcan) dummy, so the WPILib HAL can initialize
#     (it aborts unless all of can_s0..can_s4 exist).
#
# So with NO HAT, all of can_s0..4 become virtual and you drive devices over the
# CANivore:  new CANBus("reefmaster").  (The CANivore is can2 / USB and is independent
# of these buses.)
#
# Force ALL buses virtual even when the HAT is fitted (e.g. CANivore-only) by creating:
#     sudo touch /etc/diy-can-virtual-only
set +e

FORCE_VIRTUAL=0
[ -e /etc/diy-can-virtual-only ] && FORCE_VIRTUAL=1

ifname_for() { # $1 = spi path (e.g. spi0.0); prints the netdev bound to it, else empty
  local d
  for d in /sys/class/net/*/device; do
    [ -e "$d" ] || continue
    if [ "$(basename "$(readlink -f "$d")")" = "$1" ]; then
      basename "$(dirname "$d")"
      return 0
    fi
  done
  return 1
}

make_vcan() { # $1 = name
  if ! ip link show "$1" >/dev/null 2>&1; then
    ip link add "$1" type vcan 2>/dev/null
    ip link set "$1" mtu 72 2>/dev/null
  fi
  ip link set "$1" up 2>/dev/null
}

setup_bus() { # $1 = spi path ("" = none/virtual), $2 = target name
  local cur=""
  if [ "$FORCE_VIRTUAL" = "0" ] && [ -n "$1" ]; then
    cur="$(ifname_for "$1")"
  fi
  if [ -n "$cur" ]; then
    ip link set "$cur" down 2>/dev/null
    if [ "$cur" != "$2" ]; then
      ip link set "$cur" name "$2" 2>/dev/null
    fi
    ip link set "$2" type can bitrate 1000000 2>/dev/null
    ip link set "$2" txqueuelen 1000 2>/dev/null
    ip link set "$2" up 2>/dev/null
    echo "diy-can: $2 = PHYSICAL ($cur on $1) @ 1Mbps"
  else
    make_vcan "$2"
    echo "diy-can: $2 = VIRTUAL (vcan dummy)"
  fi
}

modprobe vcan 2>/dev/null

# If not forcing virtual, give the CAN FD SPI controllers up to ~12s to probe (only
# matters if the HAT is fitted; if absent, the loop just times out and we go virtual).
if [ "$FORCE_VIRTUAL" = "0" ]; then
  for i in $(seq 1 12); do
    [ -n "$(ifname_for spi0.0)$(ifname_for spi1.0)" ] && break
    sleep 1
  done
fi

setup_bus spi0.0 can_s0   # CAN_0 on the Waveshare HAT
setup_bus spi1.0 can_s1   # CAN_1 on the Waveshare HAT (Mode A)
setup_bus ""      can_s2   # no physical controller on this board -> virtual
setup_bus ""      can_s3
setup_bus ""      can_s4

# robot_heartbeat depends on can_sender; modprobe pulls it. Needs CAN ifaces to exist
# (real or vcan both satisfy it).
if modprobe robot_heartbeat; then
  echo "diy-can: robot_heartbeat loaded"
else
  echo "diy-can: WARNING robot_heartbeat failed to load"
fi

exit 0
