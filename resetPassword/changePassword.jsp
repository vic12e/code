<%--
  Created by IntelliJ IDEA.
  User: nnnhi
  Date: 6/5/2015
  Time: 3:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

  <div ng-controller="userController">
      <h1 align="center">Change Password</h1>
    <div align="center">
        <div class="alert alert-success" ng-class="{'': message != '', 'none': message == null}">
            {{message}}
        </div>
      <form id="passwordForm" name="myForm">
      <div class="col-lg-9">
        <input type="password" class="input-lg form-control" ng-model="resetUser.oldPassword" required placeholder="Current Password"/><br>
        <input type="password" class="input-lg form-control" ng-model="resetUser.newPassword" required placeholder="New Password"/><br>
        <input type="password" class="input-lg form-control" ng-model="resetUser.reNewPassword" required placeholder="Confirm Password"/><br>
          <span ng-show = "match == false" class="alert-error">
              The confirm password field doesn't match the new password </span>
      </div>
          <input type="button" class="btn btn-primary btn-load btn-lg" ng-disabled="myForm.$error.required" ng-click="resetPassword();" value="Change Password" />
      </form>
    </div>
    <!--/col-sm-6-->
  </div>
  <!--/row-->


<link href="<c:url value='/resources/css/sample.css'  />" rel="stylesheet"/>
<script src="<c:url value="/resources/js/controller/userControllers.js" />"></script>
