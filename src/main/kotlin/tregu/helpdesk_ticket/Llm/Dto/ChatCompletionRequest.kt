package tregu.helpdesk_ticket.Llm.Dto


data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatMessage>
){
    data class Choice(val message: ChatMessage)
}