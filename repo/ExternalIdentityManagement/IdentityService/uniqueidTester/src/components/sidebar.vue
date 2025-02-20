<script setup lang="ts"/>

<template>
  <!-- START:: sidebar -->
  <div id="psb" class="page-sidebar">
    <!-- sidebar navigation -->
    <div class="sidebar-collapse">
      <!-- sidebar user panel -->
      <div class="d-flex user-panel">
        <div>
          <img class="user-avatar img-circle" src="/img/john-doe.png">
        </div>
        <div class="user-info">
          <div>username<i class="caret m-l-5"></i>
            <p><small>rolename</small></p>
          </div>
        </div>
      </div>
      <!-- sidebar user menu -->
      <div class="user-menu">
        <form>
          <ul class="sidebar-menu metismenu">
            <li>
              <a href="#"><i class="sidebar-item-icon fas fa-power-off"></i><span class="nav-label">{{$t("action.signout.label")}}</span></a>
            </li>
          </ul>
        </form>
      </div>
    </div>
    <!-- sidebar navigation menu -->
    <ul id="ul02" class="sidebar-menu metismenu">
      <li>
        <router-link to="/ptt"><i class="sidebar-item-icon fas fa-hand-point-up"></i><span class="nav-label">{{$t("ptt.menu")}}</span></router-link>
      </li>
      <li>
        <router-link to="/cnt"><i class="sidebar-item-icon fas fa-earth-europe"></i><span class="nav-label">{{$t("cnt.menu")}}</span></router-link>
      </li>
      <li>
        <router-link to="/sta"><i class="sidebar-item-icon fas fa-flag"></i><span class="nav-label">{{$t("sta.menu")}}</span></router-link>
      </li>
      <li>
        <router-link to="/pts"><i class="sidebar-item-icon fas fa-handshake"></i><span class="nav-label">{{$t("pts.menu")}}</span></router-link>
      </li>
    </ul>
  </div>
  <!-- END:: sidebar -->
</template>

<style scoped/>