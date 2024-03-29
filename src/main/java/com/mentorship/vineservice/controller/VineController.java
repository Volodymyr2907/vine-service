package com.mentorship.vineservice.controller;


import static com.mentorship.vineservice.dto.enums.VineColor.getVineColor;

import com.mentorship.vineservice.controller.exeption.PermissionException;
import com.mentorship.vineservice.dto.OrderDto;
import com.mentorship.vineservice.dto.VineDto;
import com.mentorship.vineservice.dto.VinesDto;
import com.mentorship.vineservice.dto.enums.UserRole;
import com.mentorship.vineservice.model.VinesQueryParameters;
import com.mentorship.vineservice.service.OrderService;
import com.mentorship.vineservice.service.PermissionValidator;
import com.mentorship.vineservice.service.VineService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vine-service")
public class VineController {

    private static final String TOTAL_COUNT_HEADER = "X-Total-Count";
    private final VineService vineService;
    private final OrderService orderService;
    private final PermissionValidator permissionValidator;

    @PostMapping("/vine")
    public ResponseEntity<Long> createVine(@Valid @RequestBody VineDto vine,
        @RequestHeader("Authorization") String requestHeader) throws PermissionException {

        permissionValidator.validateUserPermission(requestHeader, UserRole.ADMIN);
        Long createdVineId = vineService.saveVine(vine);
        return new ResponseEntity<>(createdVineId, HttpStatus.CREATED);
    }


    @GetMapping("/vines")
    public ResponseEntity<List<VineDto>> getAllVinesWithPagination(@Valid
    @RequestParam(required = false, value = "page", defaultValue = "0") Integer page,
        @RequestParam(required = false, value = "size", defaultValue = "5") Integer size,
        @RequestParam(required = false, value = "name") String name,
        @RequestParam(required = false, value = "grape") String grape,
        @RequestParam(required = false, value = "sugar") String sugar,
        @RequestParam(required = false, value = "color") String color,
        @RequestParam(required = false, value = "year") Integer year) {

        VinesQueryParameters queryParameters = VinesQueryParameters.builder()
            .page(page)
            .size(size)
            .name(name)
            .grapeName(grape)
            .sugar(sugar)
            .color(getVineColor(color))
            .year(year)
            .build();

        VinesDto vinesDto = vineService.getVinesWithFilterAndPagination(queryParameters);

        HttpHeaders headers = new HttpHeaders();
        headers.add(TOTAL_COUNT_HEADER, String.valueOf(vinesDto.getTotalCount()));

        return new ResponseEntity<>(vinesDto.getVines(), headers, HttpStatus.OK);

    }

    @PostMapping("/order")
    public ResponseEntity<Long> createOrder(@Valid @RequestBody OrderDto order,
        @RequestHeader("Authorization") String requestHeader) throws PermissionException {

        permissionValidator.validateUserPermission(requestHeader, UserRole.USER);
        Long createdOrderId = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrderId, HttpStatus.CREATED);
    }
}
