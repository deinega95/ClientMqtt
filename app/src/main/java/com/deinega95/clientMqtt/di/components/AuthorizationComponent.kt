package com.deinega95.clientMqtt.di.components

import com.deinega95.clientMqtt.di.scopes.AuthorizationScope
import com.deinega95.clientMqtt.view.fragments.InputServerFragment
import dagger.Subcomponent

@AuthorizationScope
@Subcomponent
interface AuthorizationComponent {
    fun inject(inputServerFragment: InputServerFragment)
}