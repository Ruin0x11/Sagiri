package xyz.ruin.sagiri.flow

import flow.Services
import flow.ServicesFactory

class FlowServices : ServicesFactory() {
    override fun bindServices(services: Services.Binder) {
        val key = services.getKey<Any>()
    }
}