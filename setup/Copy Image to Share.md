# Setup Images
A simple guide to duplicating an prebuilt sd card to an .img file for either CAN or CAN FD

## Prepare & Copy the SD Card to an Full Size Image
Insert the SD Card into your computer
sudo dd bs=4M if=/dev/disk4 of=bobcats-systemcore-{fd or nofd}-fullsize.img
( replace the {fd or nofd} with either option that corresponds to the version you are making )

## Shrink the Image
Execute the docker command to download and make an docker image to run the commands on:
```
docker run --rm -it \
  --privileged \
  -v "$(pwd)":/work \
  ubuntu:24.04 bash
```

From the console that is kept open as part of this command execute the following

```
apt update
apt install -y git parted e2fsprogs dosfstools util-linux

git clone https://github.com/Drewsif/PiShrink.git
cd PiShrink

./pishrink.sh /work/bobcats-systemcore-{fd or nofd}.img
```

Then you should further compress this by zipping up the .img file , this should result in a file that is roughly 700mb in size.