<template>
    <div>
        <DatePicker></DatePicker>
        <SevenDayTime></SevenDayTime>
        <TodayAppRunningTime></TodayAppRunningTime>
        <TodayRunningTime></TodayRunningTime>
    </div>
</template>
  
<script>
import SevenDayTime from './day/SevenDayTime.vue'
import TodayAppRunningTime from './day/TodayAppRunningTime.vue'
import TodayRunningTime from './day/TodayRunningTime.vue'
import DatePicker from './day/DatePicker.vue'

export default {
    name: 'App',
    components: {
        SevenDayTime,
        TodayAppRunningTime,
        TodayRunningTime,
        DatePicker
    }
}
</script>