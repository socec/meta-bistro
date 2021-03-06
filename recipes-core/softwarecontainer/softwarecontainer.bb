#
#   Copyright (C) 2016 - 2017 Pelagicore AB
#   All rights reserved.
#

DESCRIPTION = "The SoftwareContainer framework"
HOMEPAGE = "https://github.com/Pelagicore/softwarecontainer"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4fbd65380cdd255951079008b364516c"
FILESEXTRAPATHS_append := ":${THISDIR}/files"

PR = "r0"
PV = "1.0+git${SRCREV}"

SRC_URI = "gitsm://github.com/Pelagicore/softwarecontainer.git;protocol=https;branch=master"
SRC_URI += "\
    file://softwarecontainer-agent.service \
"
SRCREV = "4ec2208e5a09b96bf911e6f2c39d8e56beccac75"

DEPENDS = "ivi-logging glibmm lxc jansson"

inherit cmake systemd pkgconfig

S = "${WORKDIR}/git/"

PACKAGECONFIG[pulsegateway] = "-DENABLE_PULSEGATEWAY=ON,-DENABLE_PULSEGATEWAY=OFF,pulseaudio"
PACKAGECONFIG[networkgateway] = "-DENABLE_NETWORKGATEWAY=ON,-DENABLE_NETWORKGATEWAY=OFF,,iptables bridge-utils"
PACKAGECONFIG[devicenodegateway] = "-DENABLE_DEVICENODEGATEWAY=ON,-DENABLE_DEVICENODEGATEWAY=OFF"
PACKAGECONFIG[cgroupsgateway] = "-DENABLE_CGROUPSGATEWAY=ON,-DENABLE_CGROUPSGATEWAY=OFF"
PACKAGECONFIG ?= "networkgateway devicenodegateway dbusgateway cgroupsgateway"

EXTRA_OECMAKE += "-DENABLE_TEST=OFF -DENABLE_EXAMPLES=ON -DENABLE_SYSTEMD=OFF"

SYSTEMD_SERVICE_${PN} = "softwarecontainer-agent.service"

PACKAGES = "${PN}-examples ${PN} ${PN}-dev ${PN}-dbg ${PN}-doc ${PN}-locale"

do_install_append() {
    install -d ${D}/lib/systemd/system/
    install -m 0644 ${S}/../softwarecontainer-agent.service ${D}/lib/systemd/system/
}

FILES_${PN} += " \
    ${libdir}/libsoftwarecontainercommon.so \
    ${systemd_unitdir}/system \
    ${sysconfdir}/dbus-1 \
    /lib/systemd/system/softwarecontainer-agent.service \
"
FILES_${PN}-examples = "${datadir}/softwarecontainer/examples/"
FILES_${PN}-dbg += "${datadir}/softwarecontainer/examples/*/.debug"

INSANE_SKIP_${PN} += "useless-rpaths"
INSANE_SKIP_${PN}-dev += "useless-rpaths"
