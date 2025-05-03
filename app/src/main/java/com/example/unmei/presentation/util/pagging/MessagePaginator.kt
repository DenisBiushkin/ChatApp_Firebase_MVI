package com.example.unmei.presentation.util.pagging

class MessagePaginator<Key,Item>(
    private val initKey:Key,
    private inline val onLoadUpdated:(Boolean)->Unit,
    private inline val onRequest:suspend (nextKey:Key)->Result<List<Item>>,
    private inline val getNextKey: suspend (List<Item> )->Key,
    private inline val onError: suspend (Throwable?)->Unit,
    private inline val onSuccess: suspend (items:List<Item>,newKey:Key)->Unit
) :Paginator<Key,Item>{

    private var currentKey:Key = initKey
    private var isMakingRequest: Boolean = false
    override suspend fun loadNextItems() {
        if (isMakingRequest){
            return
        }
        //чтобы небыло двойных запросов на эти же страницы
        isMakingRequest=true
        //сообщаем что начали загрузку
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest=false
        val items=result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentKey =getNextKey(items)
        onSuccess(items,currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initKey
    }
}