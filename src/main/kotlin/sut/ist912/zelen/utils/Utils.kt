package sut.ist912.zelen.utils

import sut.ist912.zelen.rest.LoginClient

fun Double.format(digits: Int = 2) = "%.${digits}f".format(this)


fun Jwt() = LoginClient.jwtToken