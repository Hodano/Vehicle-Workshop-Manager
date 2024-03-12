package pl.hodan.carservice.common.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.hodan.carservice.DTO.UserDTO;
import pl.hodan.carservice.DTO.UserDTOPassword;
import pl.hodan.carservice.common.entity.Car;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.vindecoder.model.VINDecoderModel;

@Configuration
public class MapperConfiguration {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<User, UserDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setEmail(source.getEmail());
                map().setName(source.getUserBasicInformation().getName());
                map().setSurname(source.getUserBasicInformation().getSurname());
                map().setAddress(source.getUserBasicInformation().getAddress());
                map().setPhoneNumber(source.getUserBasicInformation().getPhoneNumber());
                map().setRoles(source.getRoles());
            }
        });

        modelMapper.addMappings(new PropertyMap<User, UserDTOPassword>() {
            @Override
            protected void configure() {
                map().setPassword(source.getPassword());
                map().setEmail(source.getEmail());
                map().setName(source.getUserBasicInformation().getName());
                map().setSurname(source.getUserBasicInformation().getSurname());
                map().setAddress(source.getUserBasicInformation().getAddress());
                map().setPhoneNumber(source.getUserBasicInformation().getPhoneNumber());

            }
        });
        modelMapper.addMappings(new PropertyMap<UserDTOPassword, User>() {
            @Override
            protected void configure() {
                map().getUserBasicInformation().setName(source.getName());
                map().getUserBasicInformation().setSurname(source.getSurname());
                map().getUserBasicInformation().setAddress(source.getAddress());
                map().getUserBasicInformation().setPhoneNumber(source.getPhoneNumber());

                map().setEmail(source.getEmail());
                skip(destination.getId());


            }
        });
        ///VIN?CAR
        modelMapper.addMappings(new PropertyMap<VINDecoderModel, Car>() {
            @Override
            protected void configure() {
                map().setVIN(source.getVIN());
                map().setRegion(source.getRegion());
                map().setCountry(source.getCountry());
                map().setMake(source.getMake());
                map().setModel(source.getModel());
                map().setModelYear(source.getModelYear());
                map().setBodyStyle(source.getBodyStyle());
                map().setEngineType(source.getEngineType());
                map().setFuelType(source.getFuelType());
                map().setTransmission(source.getTransmission());
                map().setManufacturedIn(source.getManufacturedIn());
                map().setBodyType(source.getBodyType());
                map().setNumberOfDoors(source.getNumberOfDoors());
                map().setNumberOfSeats(source.getNumberOfSeats());

//                skip(destination.getId());
            }
        });


        return modelMapper;
    }
}
