package com.squrlabs.sca.domain.model.socket

data class SocketModel<T>(
        val type: SocketType,
        val data: T
)