<script setup lang="ts">
  import toolbar from '@/components/toolbar.vue';
  import sidebar from '@/components/sidebar.vue';
</script>

<template>
  <toolbar/>
  <sidebar/>
  <div class="content-wrapper">
     <!-- START:: pagecontent -->
     <div id="content" layout="block">
      <router-view default="/"/>
     </div>
     <!-- END:: pagecontent -->
  </div>
</template>

<style lang="scss">
</style>