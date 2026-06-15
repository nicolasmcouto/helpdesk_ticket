package tregu.helpdesk_ticket.Service


import org.springframework.stereotype.Service
import tregu.helpdesk_ticket.domain.Repository.TagRepository

@Service
class TagService(private val tagRepository: TagRepository){

}