package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.MyDevice;
import ng.com.justjava.corebanking.service.dto.MyDeviceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link MyDevice} and its DTO {@link MyDeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface MyDeviceMapper extends EntityMapper<MyDeviceDTO, MyDevice> {

    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "profile.phoneNumber", target = "phoneNumber")
    MyDeviceDTO toDto(MyDevice myDevice);

    @Mapping(source = "profileId", target = "profile")
    MyDevice toEntity(MyDeviceDTO myDeviceDTO);

    default MyDevice fromId(Long id) {
        if (id == null) {
            return null;
        }
        MyDevice myDevice = new MyDevice();
        myDevice.setId(id);
        return myDevice;
    }
}
