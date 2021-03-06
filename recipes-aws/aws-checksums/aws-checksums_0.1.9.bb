DESCRIPTION = "Cross-Platform HW accelerated CRC32c and CRC32 with fallback to efficient SW implementations."
AUTHOR = "Amazon"
HOMEPAGE = "https://github.com/awslabs/aws-checksums"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit cmake

DEPENDS += "\
    aws-c-common \
"

SRC_URI = "\
    git://github.com/awslabs/${BPN}.git;branch=master;tag=v${PV} \
"

PR = "r0"

S = "${WORKDIR}/git"

EXTRA_OECMAKE += "\
    -DBUILD_SHARED_LIBS=ON \
    -DBUILD_TESTING=OFF \
    -DCMAKE_PREFIX_PATH=${RECIPE_SYSROOT}/usr \
"
