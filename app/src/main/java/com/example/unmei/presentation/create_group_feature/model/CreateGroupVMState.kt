package com.example.unmei.presentation.create_group_feature.model

data class CreateGroupVMState(
    val chatName:String="",
    val contentState: CreateGroupContentState= CreateGroupContentState.LOADING,
    val groupedFriends: Map<String,List<CreateGroupItemUi>> = emptyMap(),
    val selectedUsersIds: Set<String> = emptySet()
)

