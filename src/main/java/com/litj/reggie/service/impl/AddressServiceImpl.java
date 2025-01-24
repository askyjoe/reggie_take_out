package com.litj.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.litj.reggie.entity.AddressBook;
import com.litj.reggie.mapper.AddressBookMapper;
import com.litj.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
