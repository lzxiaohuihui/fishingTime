<template>
    <div>
        <div class="block">
            <!-- <span class="demonstration">带快捷选项</span> -->
            <el-date-picker v-model="value" @change="onDatePick" type="daterange" align="right" unlink-panels
                range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions">
            </el-date-picker>
        </div>
    </div>
</template>
  
<script>
import { eventBus } from '@/main.js'

export default {
    data() {
        return {
            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now();
                },
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
            value: ''
        };
    },
    methods: {
        onDatePick(value) {
            const date1 = new Date(value[0]);
            const year1 = date1.getFullYear();
            const month1 = date1.getMonth() + 1;
            const day1 = date1.getDate();
            const dateString1 = `${year1}-${month1 < 10 ? '0' + month1 : month1}-${day1 < 10 ? '0' + day1 : day1}`;

            const date2 = new Date(value[1]);
            const year2 = date2.getFullYear();
            const month2 = date2.getMonth() + 1;
            const day2 = date2.getDate();
            const dateString2 = `${year2}-${month2 < 10 ? '0' + month2 : month2}-${day2 < 10 ? '0' + day2 : day2}`;

            const params = [dateString1, dateString2];
            eventBus.$emit('date-scope-selected', params)
        }
    }
};
</script>