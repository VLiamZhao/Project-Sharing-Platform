package com.psp.controller;

import com.psp.model.Role;
import com.psp.model.User;
import com.psp.model.UserInfo;
import com.psp.repository.UserInfoDao;
import com.psp.service.JWTService;
import com.psp.service.RoleService;
import com.psp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
@RestController
@RequestMapping(
        value = {"/auth"},
        method = RequestMethod.POST)
public class AuthController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired UserService userService;
  @Autowired RoleService roleService;
  @Autowired JWTService jwtService;
  @Autowired UserInfoDao userInfoDao;

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<Map<String, String>> userLogin(@RequestBody User user) {
    try {
      User tempUser = userService.getUserByCredentials(user.getEmail(), user.getPassword());
      assert (tempUser != null);
      String token = jwtService.generateToken(tempUser);
      Map<String, String> m = new HashMap<>();
      m.put("token", token);
      return ResponseEntity.ok().body(m);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @RequestMapping(value = "/registration", method = RequestMethod.POST)
  public ResponseEntity<Map<String, String>> userSignUp(@RequestBody User user) {
    try {
      Role role;
      if (user.getUserType().equals("student")) {
        role = roleService.getRoleByName(Role.studentRole);
      } else {
        role = roleService.getRoleByName(Role.companyRole);
      }
      UserInfo userInfo = new UserInfo();
      user.setUserInfo(userInfo);
      userInfo.setUser(user);
      user.setRole(role);
      User savedUser = userService.save(user);
      userInfoDao.save(userInfo);
      if (savedUser == null)
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).build();

      //            return ResponseEntity.status(HttpServletResponse.SC_OK).body(savedUser);
      Map<String, String> map = new HashMap<>();
      //            map.put("Id", String.valueOf(savedUser.getId()));
      //            map.put("Username", savedUser.getUsername());
      //            map.put("Email", savedUser.getEmail());
      //            map.put("User Type", savedUser.getUserType());
      map.put("Result", "Success");
      return ResponseEntity.ok().body(map);
    } catch (Exception e) {
      e.printStackTrace();
    }
    //        return ResponseEntity.ok(savedUser);
    return null;
  }
}
