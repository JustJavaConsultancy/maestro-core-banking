package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.ContactUs;
import ng.com.systemspecs.apigateway.service.dto.ContactUsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link ContactUs} and its DTO {@link ContactUsDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface ContactUsMapper extends EntityMapper<ContactUsDTO, ContactUs> {

    @Mapping(source = "sender.phoneNumber", target = "senderPhoneNumber")
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "assignedTo.phoneNumber", target = "assignedToPhoneNumber")
    @Mapping(source = "assignedTo.id", target = "assignedToId")
    ContactUsDTO toDto(ContactUs contactUs);

    @Mapping(source = "senderId", target = "sender")
    @Mapping(source = "assignedToId", target = "assignedTo")
    ContactUs toEntity(ContactUsDTO contactUsDTO);

    default ContactUs fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContactUs contactUs = new ContactUs();
        contactUs.setId(id);
        return contactUs;
    }

}
