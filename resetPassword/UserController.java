package com.tma.gbst.controller;

import com.sun.tracing.dtrace.ModuleAttributes;
import com.tma.gbst.model.Role;
import com.tma.gbst.model.Team;
import com.tma.gbst.model.User;
import com.tma.gbst.service.TeamService;
import com.tma.gbst.service.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Nghi Tran on 4/21/2015.
 */

@Controller
@RequestMapping(value = "/protected/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @RequestMapping(method = RequestMethod.GET)
     public ModelAndView welcome () {
        return new ModelAndView("userList");
    }

    @RequestMapping(value = "/userList", method = RequestMethod.GET, headers = "Accept=application/json")
     public @ResponseBody
     String listUsersJson() throws JSONException {
        JSONArray userArray = new JSONArray();

        // add only one team array to userJSON[0]
        JSONArray teamArray = new JSONArray();
        for (Team team : teamService.findAll()) {
            JSONObject teamJson = new JSONObject();
            try {
                //teamJson.put("id", team.getId());
                Integer s = team.getId();

                teamJson.put("name", team.getName());
                teamJson.put("id", team.getId());
                teamArray.put(teamJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        boolean allowArray0 = true;
        for (User user : userService.findAll()) {
            // add teams which a user has
            JSONArray userHasTeam = new JSONArray();
            for (Team teamUser : user.getTeams()) {
                JSONObject team = new JSONObject();
                team.put("name", teamUser.getName());
                team.put("id", teamUser.getId());
                userHasTeam.put(team);
            }
            JSONObject userJSON = new JSONObject();
            userJSON.put("id", user.getId());
            userJSON.put("name", user.getName());
            userJSON.put("email", user.getEmail());
            userJSON.put("userTeams",userHasTeam);
            // just at userJSON[0]
            if(allowArray0) {
                userJSON.put("teams", teamArray);
            }

            userArray.put(userJSON);
        }
        return userArray.toString();
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(@RequestBody String newUser) {
        try {
            //set user variables
            JSONObject userJSON = new JSONObject(newUser);
            User user = new User();
            int id = Integer.parseInt(userJSON.getString("id"));
            if(id != 0) {
                user.setId(id);
                System.out.println(user.getId());
            }
            user.setEmail(userJSON.getString("email"));
            user.setName(userJSON.getString("name"));
            user.setEnabled("YES");
            user.setPassword("123");
            user.setRole(Role.ROLE_USER);

            //set team variable
            Team t = new Team();
            int teamId = Integer.parseInt(userJSON.getString("teamId"));
            t.setId(teamId);
            // find team have teamId == id in team table
            for(Team teamInTable : teamService.findAll()){
                if ((teamInTable.getId() == teamId)){
                    t.setName(teamInTable.getName());
                }
            }
            t.setProjectInformation(null);
            t.setProject(null);

            // JPA add user to user table. Add team to team table
            t.getUsers().add(user);
            user.getTeams().add(t);

            // save user using JPA mapping of User to mutual table
            User check = userService.save(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "redirect:/protected/user";
    }

    @RequestMapping(value = "/deleteUser/{userId}", method=RequestMethod.DELETE)
    public @ResponseBody void deleteUser(@PathVariable("userId") Integer userId) {
        userService.delete(userId);
    }
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public ModelAndView changePassword () {
        return new ModelAndView("changePassword");
    }
    @RequestMapping(value = "/resetPassword", method=RequestMethod.POST)
    public @ResponseBody String resetPassword(@RequestBody String resetUser, HttpSession session) {
        User user = (User) session.getAttribute("user");
        JSONObject redirect = new JSONObject();
        try {
            JSONObject result = new JSONObject(resetUser);
            String oldPassword = result.getString("oldPassword");
        if (oldPassword.equals(user.getPassword())) {
            user.setPassword(result.getString("newPassword"));
            userService.save(user);
            redirect.put("redirect", "/protected/home/");
            redirect.put("message","Change Password Successful");
        } else {
            redirect.put("redirect", "/protected/user/changePassword");
            redirect.put("message", "Your Password is Wrong. Please Retype!");
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return redirect.toString();
    }




    /*@RequestMapping(value="/changePassword", method = RequestMethod.GET)
    public String changePass(){
        return "changePassword";
    }*/

//    @RequestMapping(method = RequestMethod.GET)
//    public ModelAndView welcome() {
//        return new ModelAndView("team");
//    }
//
//    @RequestMapping(value = "/teamList", method = RequestMethod.GET)
//    public @ResponseBody String listTeams() {
//        JSONArray teamArray = new JSONArray();
//        for (Team team : teamService.findAll()) {
//            JSONObject teamJson = new JSONObject();
//            try {
//                teamJson.put("id", team.getId());
//                teamJson.put("name", team.getName());
//                teamArray.put(teamJson);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return teamArray.toString();
//    }
}
