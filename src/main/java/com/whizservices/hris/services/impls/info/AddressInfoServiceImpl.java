package com.whizservices.hris.services.impls.info;

import com.whizservices.hris.dtos.info.AddressInfoDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.entities.info.AddressInfo;
import com.whizservices.hris.entities.profile.Employee;
import com.whizservices.hris.repositories.info.AddressInfoRepository;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
import com.whizservices.hris.repositories.reference.BarangayRepository;
import com.whizservices.hris.repositories.reference.MunicipalityRepository;
import com.whizservices.hris.repositories.reference.ProvinceRepository;
import com.whizservices.hris.repositories.reference.RegionRepository;
import com.whizservices.hris.services.impls.profile.EmployeeServiceImpl;
import com.whizservices.hris.services.impls.reference.BarangayServiceImpl;
import com.whizservices.hris.services.impls.reference.MunicipalityServiceImpl;
import com.whizservices.hris.services.impls.reference.ProvinceServiceImpl;
import com.whizservices.hris.services.impls.reference.RegionServiceImpl;
import com.whizservices.hris.services.info.AddressInfoService;
import com.whizservices.hris.services.profile.EmployeeService;
import com.whizservices.hris.services.reference.BarangayService;
import com.whizservices.hris.services.reference.MunicipalityService;
import com.whizservices.hris.services.reference.ProvinceService;
import com.whizservices.hris.services.reference.RegionService;

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
public class AddressInfoServiceImpl implements AddressInfoService {
    private final Logger logger = LoggerFactory.getLogger(AddressInfoServiceImpl.class);

    private final AddressInfoRepository addressInfoRepository;
    private final EmployeeRepository employeeRepository;
    private final BarangayRepository barangayRepository;
    private final MunicipalityRepository municipalityRepository;
    private final ProvinceRepository provinceRepository;
    private final RegionRepository regionRepository;

    public AddressInfoServiceImpl(AddressInfoRepository addressInfoRepository,
                                  EmployeeRepository employeeRepository,
                                  BarangayRepository barangayRepository,
                                  MunicipalityRepository municipalityRepository,
                                  ProvinceRepository provinceRepository,
                                  RegionRepository regionRepository) {
        this.addressInfoRepository = addressInfoRepository;
        this.employeeRepository = employeeRepository;
        this.barangayRepository = barangayRepository;
        this.municipalityRepository = municipalityRepository;
        this.provinceRepository = provinceRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    public void saveOrUpdate(AddressInfoDTO object) {
        AddressInfo addressInfo;
        String logMessage;

        if (object.getId() != null) {
            addressInfo = addressInfoRepository.getReferenceById(object.getId());
            logMessage = "Address record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            addressInfo = new AddressInfo();
            addressInfo.setCreatedBy(object.getCreatedBy());
            addressInfo.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Address record is successfully created.";
        }

        addressInfo.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        addressInfo.setAddressType(object.getAddressType());
        addressInfo.setAddressDetail(object.getAddressDetail());
        addressInfo.setStreetName(object.getStreetName());
        addressInfo.setBarangay(barangayRepository.getReferenceById(object.getBarangayDTO().getId()));
        addressInfo.setMunicipality(municipalityRepository.getReferenceById(object.getMunicipalityDTO().getId()));
        addressInfo.setProvince(provinceRepository.getReferenceById(object.getProvinceDTO().getId()));
        addressInfo.setRegion(regionRepository.getReferenceById(object.getRegionDTO().getId()));
        addressInfo.setPostalCode(object.getPostalCode());
        addressInfo.setUpdatedBy(object.getUpdatedBy());
        addressInfo.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        addressInfoRepository.save(addressInfo);
        logger.info(logMessage);
    }

    @Override
    public AddressInfoDTO getById(UUID id) {
        logger.info("Retrieving personal address record with UUID ".concat(id.toString()));

        AddressInfo addressInfo = addressInfoRepository.getReferenceById(id);
        AddressInfoDTO addressInfoDTO = new AddressInfoDTO();

        addressInfoDTO.setId(addressInfo.getId());
        addressInfoDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(addressInfo.getEmployee().getId()));
        addressInfoDTO.setAddressType(addressInfo.getAddressType());
        addressInfoDTO.setAddressDetail(addressInfo.getAddressDetail());
        addressInfoDTO.setStreetName(addressInfo.getStreetName());
        addressInfoDTO.setBarangayDTO(new BarangayServiceImpl(barangayRepository).getById(addressInfo.getBarangay().getId()));
        addressInfoDTO.setMunicipalityDTO(new MunicipalityServiceImpl(municipalityRepository).getById(addressInfo.getMunicipality().getId()));
        addressInfoDTO.setProvinceDTO(new ProvinceServiceImpl(provinceRepository).getById(addressInfo.getProvince().getId()));
        addressInfoDTO.setRegionDTO(new RegionServiceImpl(regionRepository).getById(addressInfo.getRegion().getId()));
        addressInfoDTO.setPostalCode(addressInfo.getPostalCode());

        return addressInfoDTO;
    }

    @Override
    public void delete(AddressInfoDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the address record permanently.");

            String id = object.getId().toString();
            AddressInfo addressInfo = addressInfoRepository.getReferenceById(object.getId());
            addressInfoRepository.delete(addressInfo);

            logger.info("Address record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<AddressInfoDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving address records from the database.");
        List<AddressInfo> addressInfoList = addressInfoRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Address records successfully retrieved.");
        List<AddressInfoDTO> addressInfoDTOList = new ArrayList<>();

        if (!addressInfoList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);
            BarangayService barangayService = new BarangayServiceImpl(barangayRepository);
            MunicipalityService municipalityService = new MunicipalityServiceImpl(municipalityRepository);
            ProvinceService provinceService = new ProvinceServiceImpl(provinceRepository);
            RegionService regionService = new RegionServiceImpl(regionRepository);

            for (AddressInfo addressInfo : addressInfoList) {
                AddressInfoDTO addressInfoDTO = new AddressInfoDTO();

                addressInfoDTO.setId(addressInfo.getId());
                addressInfoDTO.setEmployeeDTO(employeeService.getById(addressInfo.getEmployee().getId()));
                addressInfoDTO.setAddressType(addressInfo.getAddressType());
                addressInfoDTO.setAddressDetail(addressInfo.getAddressDetail());
                addressInfoDTO.setStreetName(addressInfo.getStreetName());
                addressInfoDTO.setBarangayDTO(barangayService.getById(addressInfo.getBarangay().getId()));
                addressInfoDTO.setMunicipalityDTO(municipalityService.getById(addressInfo.getMunicipality().getId()));
                addressInfoDTO.setProvinceDTO(provinceService.getById(addressInfo.getProvince().getId()));
                addressInfoDTO.setRegionDTO(regionService.getById(addressInfo.getRegion().getId()));
                addressInfoDTO.setPostalCode(addressInfo.getPostalCode());

                addressInfoDTOList.add(addressInfoDTO);
            }

            logger.info(String.valueOf(addressInfoList.size()).concat(" record(s) found."));
        }

        return addressInfoDTOList;
    }

    @Override
    public List<AddressInfoDTO> findByParameter(String param) {
        return List.of();
    }

    @Override
    public List<AddressInfoDTO> getByEmployeeDTO(EmployeeDTO employeeDTO) {
        logger.info("Retrieving address records with employee UUID ".concat(employeeDTO.getId().toString()));

        Employee employee = employeeRepository.getReferenceById(employeeDTO.getId());

        List<AddressInfo> addressInfoList = addressInfoRepository.findByEmployee(employee);
        List<AddressInfoDTO> addressInfoDTOList = new ArrayList<>();

        if (!addressInfoList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);
            BarangayService barangayService = new BarangayServiceImpl(barangayRepository);
            MunicipalityService municipalityService = new MunicipalityServiceImpl(municipalityRepository);
            ProvinceService provinceService = new ProvinceServiceImpl(provinceRepository);
            RegionService regionService = new RegionServiceImpl(regionRepository);

            for (AddressInfo addressInfo : addressInfoList) {
                AddressInfoDTO addressInfoDTO = new AddressInfoDTO();

                addressInfoDTO.setId(addressInfo.getId());
                addressInfoDTO.setEmployeeDTO(employeeService.getById(addressInfo.getEmployee().getId()));
                addressInfoDTO.setAddressType(addressInfo.getAddressType());
                addressInfoDTO.setAddressDetail(addressInfo.getAddressDetail());
                addressInfoDTO.setStreetName(addressInfo.getStreetName());
                addressInfoDTO.setBarangayDTO(barangayService.getById(addressInfo.getBarangay().getId()));
                addressInfoDTO.setMunicipalityDTO(municipalityService.getById(addressInfo.getMunicipality().getId()));
                addressInfoDTO.setProvinceDTO(provinceService.getById(addressInfo.getProvince().getId()));
                addressInfoDTO.setRegionDTO(regionService.getById(addressInfo.getRegion().getId()));
                addressInfoDTO.setPostalCode(addressInfo.getPostalCode());

                addressInfoDTOList.add(addressInfoDTO);
            }

            logger.info(String.valueOf(addressInfoList.size()).concat(" record(s) found."));
        }

        return addressInfoDTOList;
    }
}
