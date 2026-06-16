package tregu.helpdesk_ticket.Llm.Dto


data class ChatCompletionResponse(
    val choices: List<Choice>
){
    data class Choice(val message: ChatMessage)
}
