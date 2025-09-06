package io.softwaregarage.hris.profile.services.impls;

import io.softwaregarage.hris.profile.dtos.AddressProfileDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.entities.AddressProfile;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;
import io.softwaregarage.hris.profile.repositories.AddressProfileRepository;
import io.softwaregarage.hris.profile.repositories.EmployeeProfileRepository;
import io.softwaregarage.hris.admin.repositories.BarangayRepository;
import io.softwaregarage.hris.admin.repositories.MunicipalityRepository;
import io.softwaregarage.hris.admin.repositories.ProvinceRepository;
import io.softwaregarage.hris.admin.repositories.RegionRepository;
import io.softwaregarage.hris.admin.services.impls.BarangayServiceImpl;
import io.softwaregarage.hris.admin.services.impls.MunicipalityServiceImpl;
import io.softwaregarage.hris.admin.services.impls.ProvinceServiceImpl;
import io.softwaregarage.hris.admin.services.impls.RegionServiceImpl;
import io.softwaregarage.hris.profile.services.AddressProfileService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.admin.services.BarangayService;
import io.softwaregarage.hris.admin.services.MunicipalityService;
import io.softwaregarage.hris.admin.services.ProvinceService;
import io.softwaregarage.hris.admin.services.RegionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AddressProfileServiceImpl implements AddressProfileService {
    private final Logger logger = LoggerFactory.getLogger(AddressProfileServiceImpl.class);

    private final AddressProfileRepository addressProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final BarangayRepository barangayRepository;
    private final MunicipalityRepository municipalityRepository;
    private final ProvinceRepository provinceRepository;
    private final RegionRepository regionRepository;

    public AddressProfileServiceImpl(AddressProfileRepository addressProfileRepository,
                                     EmployeeProfileRepository employeeProfileRepository,
                                     BarangayRepository barangayRepository,
                                     MunicipalityRepository municipalityRepository,
                                     ProvinceRepository provinceRepository,
                                     RegionRepository regionRepository) {
        this.addressProfileRepository = addressProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.barangayRepository = barangayRepository;
        this.municipalityRepository = municipalityRepository;
        this.provinceRepository = provinceRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    public void saveOrUpdate(AddressProfileDTO object) {
        AddressProfile addressProfile;
        String logMessage;

        if (object.getId() != null) {
            addressProfile = addressProfileRepository.getReferenceById(object.getId());
            logMessage = "Address record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            addressProfile = new AddressProfile();
            addressProfile.setCreatedBy(object.getCreatedBy());
            addressProfile.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Address record is successfully created.";
        }

        addressProfile.setEmployee(employeeProfileRepository.getReferenceById(object.getEmployeeDTO().getId()));
        addressProfile.setAddressType(object.getAddressType());
        addressProfile.setAddressDetail(object.getAddressDetail());
        addressProfile.setStreetName(object.getStreetName());
        addressProfile.setBarangay(barangayRepository.getReferenceById(object.getBarangayDTO().getId()));
        addressProfile.setMunicipality(municipalityRepository.getReferenceById(object.getMunicipalityDTO().getId()));
        addressProfile.setProvince(provinceRepository.getReferenceById(object.getProvinceDTO().getId()));
        addressProfile.setRegion(regionRepository.getReferenceById(object.getRegionDTO().getId()));
        addressProfile.setPostalCode(object.getPostalCode());
        addressProfile.setUpdatedBy(object.getUpdatedBy());
        addressProfile.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        addressProfileRepository.save(addressProfile);
        logger.info(logMessage);
    }

    @Override
    public AddressProfileDTO getById(UUID id) {
        logger.info("Retrieving personal address record with UUID ".concat(id.toString()));

        AddressProfile addressProfile = addressProfileRepository.getReferenceById(id);
        AddressProfileDTO addressProfileDTO = new AddressProfileDTO();

        addressProfileDTO.setId(addressProfile.getId());
        addressProfileDTO.setEmployeeDTO(new EmployeeProfileServiceImpl(employeeProfileRepository).getById(addressProfile.getEmployee().getId()));
        addressProfileDTO.setAddressType(addressProfile.getAddressType());
        addressProfileDTO.setAddressDetail(addressProfile.getAddressDetail());
        addressProfileDTO.setStreetName(addressProfile.getStreetName());
        addressProfileDTO.setBarangayDTO(new BarangayServiceImpl(barangayRepository).getById(addressProfile.getBarangay().getId()));
        addressProfileDTO.setMunicipalityDTO(new MunicipalityServiceImpl(municipalityRepository).getById(addressProfile.getMunicipality().getId()));
        addressProfileDTO.setProvinceDTO(new ProvinceServiceImpl(provinceRepository).getById(addressProfile.getProvince().getId()));
        addressProfileDTO.setRegionDTO(new RegionServiceImpl(regionRepository).getById(addressProfile.getRegion().getId()));
        addressProfileDTO.setPostalCode(addressProfile.getPostalCode());

        return addressProfileDTO;
    }

    @Override
    public void delete(AddressProfileDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the address record permanently.");

            String id = object.getId().toString();
            AddressProfile addressProfile = addressProfileRepository.getReferenceById(object.getId());
            addressProfileRepository.delete(addressProfile);

            logger.info("Address record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<AddressProfileDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving address records from the database.");
        List<AddressProfile> addressProfileList = addressProfileRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Address records successfully retrieved.");
        List<AddressProfileDTO> addressProfileDTOList = new ArrayList<>();

        if (!addressProfileList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);
            BarangayService barangayService = new BarangayServiceImpl(barangayRepository);
            MunicipalityService municipalityService = new MunicipalityServiceImpl(municipalityRepository);
            ProvinceService provinceService = new ProvinceServiceImpl(provinceRepository);
            RegionService regionService = new RegionServiceImpl(regionRepository);

            for (AddressProfile addressProfile : addressProfileList) {
                AddressProfileDTO addressProfileDTO = new AddressProfileDTO();

                addressProfileDTO.setId(addressProfile.getId());
                addressProfileDTO.setEmployeeDTO(employeeProfileService.getById(addressProfile.getEmployee().getId()));
                addressProfileDTO.setAddressType(addressProfile.getAddressType());
                addressProfileDTO.setAddressDetail(addressProfile.getAddressDetail());
                addressProfileDTO.setStreetName(addressProfile.getStreetName());
                addressProfileDTO.setBarangayDTO(barangayService.getById(addressProfile.getBarangay().getId()));
                addressProfileDTO.setMunicipalityDTO(municipalityService.getById(addressProfile.getMunicipality().getId()));
                addressProfileDTO.setProvinceDTO(provinceService.getById(addressProfile.getProvince().getId()));
                addressProfileDTO.setRegionDTO(regionService.getById(addressProfile.getRegion().getId()));
                addressProfileDTO.setPostalCode(addressProfile.getPostalCode());

                addressProfileDTOList.add(addressProfileDTO);
            }

            logger.info(String.valueOf(addressProfileList.size()).concat(" record(s) found."));
        }

        return addressProfileDTOList;
    }

    @Override
    public List<AddressProfileDTO> findByParameter(String param) {
        return List.of();
    }

    @Override
    public List<AddressProfileDTO> getByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        logger.info("Retrieving address records with employee UUID ".concat(employeeProfileDTO.getId().toString()));

        EmployeeProfile employeeProfile = employeeProfileRepository.getReferenceById(employeeProfileDTO.getId());

        List<AddressProfile> addressProfileList = addressProfileRepository.findByEmployee(employeeProfile);
        List<AddressProfileDTO> addressProfileDTOList = new ArrayList<>();

        if (!addressProfileList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);
            BarangayService barangayService = new BarangayServiceImpl(barangayRepository);
            MunicipalityService municipalityService = new MunicipalityServiceImpl(municipalityRepository);
            ProvinceService provinceService = new ProvinceServiceImpl(provinceRepository);
            RegionService regionService = new RegionServiceImpl(regionRepository);

            for (AddressProfile addressProfile : addressProfileList) {
                AddressProfileDTO addressProfileDTO = new AddressProfileDTO();

                addressProfileDTO.setId(addressProfile.getId());
                addressProfileDTO.setEmployeeDTO(employeeProfileService.getById(addressProfile.getEmployee().getId()));
                addressProfileDTO.setAddressType(addressProfile.getAddressType());
                addressProfileDTO.setAddressDetail(addressProfile.getAddressDetail());
                addressProfileDTO.setStreetName(addressProfile.getStreetName());
                addressProfileDTO.setBarangayDTO(barangayService.getById(addressProfile.getBarangay().getId()));
                addressProfileDTO.setMunicipalityDTO(municipalityService.getById(addressProfile.getMunicipality().getId()));
                addressProfileDTO.setProvinceDTO(provinceService.getById(addressProfile.getProvince().getId()));
                addressProfileDTO.setRegionDTO(regionService.getById(addressProfile.getRegion().getId()));
                addressProfileDTO.setPostalCode(addressProfile.getPostalCode());

                addressProfileDTOList.add(addressProfileDTO);
            }

            logger.info(String.valueOf(addressProfileList.size()).concat(" record(s) found."));
        }

        return addressProfileDTOList;
    }
}
