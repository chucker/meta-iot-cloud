DESCRIPTION = ".NET Core Runtime, SDK & CLI tools"
HOMEPAGE = "https://www.microsoft.com/net/core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9fc642ff452b28d62ab19b7eea50dfb9"

COMPATIBLE_HOST ?= "aarch64.*-linux"

DEPENDS = "\
    zlib \
"

RDEPENDS_${PN}_class-target += "\
    icu \
    libgssapi-krb5 \
    lttng-ust \
    zlib \
"

RDEPENDS_${PN}_class-native += "\
    icu-native \
    krb5-native \
    zlib-native \
"

HOST_FXR = "5.0.0"
RUNTIME = "5.0.0"
ASP_RUNTIME = "5.0.0"
SDK = "5.0.100"

PR = "r0"

python __anonymous () {
    d.setVar('SRC_FETCH_ID', '27840e8b-d61c-472d-8e11-c16784d40091/ae9780ccda4499405cf6f0924f6f036a')
    d.setVarFlag('SRC_URI', 'sha512sum', '5fceac0a9468097d66af25516da597eb4836b294ed1647ba272ade5c8faea2ed977a95d9ce720c44d71607fa3a0cf9de55afe0e66c0c89ab1cc6736945978204')
    d.setVar('DOTNET_ARCH', 'arm64')
}

SRC_URI = "https://download.visualstudio.microsoft.com/download/pr/${SRC_FETCH_ID}/${BPN}-sdk-${SDK}-linux-${DOTNET_ARCH}.tar.gz"

S = "${WORKDIR}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

python do_install () {
    bb.build.exec_func("shell_do_install", d)
    oe.path.make_relative_symlink(d.expand("${D}${bindir}/dotnet"))
}

shell_do_install() {
    install -d ${D}${bindir}
    install -d ${D}${datadir}/dotnet
    install -d ${D}${datadir}/dotnet/host/fxr
    install -d ${D}${datadir}/dotnet/sdk
    install -d ${D}${datadir}/dotnet/shared/Microsoft.NETCore.App
    install -d ${D}${datadir}/dotnet/shared/Microsoft.AspNetCore.App

    install -m 0755 ${S}/dotnet ${D}${datadir}/dotnet
    install -m 0644 ${S}/LICENSE.txt ${D}${datadir}/dotnet
    install -m 0644 ${S}/ThirdPartyNotices.txt ${D}${datadir}/dotnet

    cp -r ${S}/sdk/${SDK} ${D}${datadir}/dotnet/sdk
    cp -r ${S}/host/fxr/${HOST_FXR} ${D}${datadir}/dotnet/host/fxr
    cp -r ${S}/shared/Microsoft.NETCore.App/${RUNTIME} ${D}${datadir}/dotnet/shared/Microsoft.NETCore.App
    cp -r ${S}/shared/Microsoft.AspNetCore.App/${ASP_RUNTIME} ${D}${datadir}/dotnet/shared/Microsoft.AspNetCore.App
    cp -r ${S}/templates ${D}${datadir}/dotnet

    # Symlinks
    ln -s ${D}${datadir}/dotnet/dotnet ${D}${bindir}/dotnet
}

FILES_${PN} = "\
    ${bindir} \
    ${datadir}/dotnet/dotnet \
    ${datadir}/dotnet/*.txt \
    ${datadir}/dotnet/host \
    ${datadir}/dotnet/shared \
"

FILES_${PN}-dev = "\
    ${datadir}/dotnet/sdk \
    ${datadir}/dotnet/templates \
"

FILES_${PN}-dbg = "\
    ${datadir}/dotnet/.debug \
"

RRECOMMENDS_dotnet-dev[nodeprrecs] = "1"

INSANE_SKIP_${PN} = "already-stripped staticdev ldflags libdir textrel"
INSANE_SKIP_${PN}-dev = "libdir"
INSANE_SKIP_${PN}-dbg += "libdir"

BBCLASSEXTEND = "native"
