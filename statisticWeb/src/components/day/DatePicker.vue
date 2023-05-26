<template>
    <div class="block">
        <!-- <span class="demonstration">带快捷选项</span> -->
        <el-date-picker v-model="value" @change="onDatePick" placeholder="选择日期" align="right" type="date"
            :picker-options="pickerOptions">
        </el-date-picker>
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
                    text: '今天',
                    onClick(picker) {
                        picker.$emit('pick', new Date());
                    }
                }, {
                    text: '昨天',
                    onClick(picker) {
                        const date = new Date();
                        date.setTime(date.getTime() - 3600 * 1000 * 24);
                        picker.$emit('pick', date);
                    }
                }, {
                    text: '一周前',
                    onClick(picker) {
                        const date = new Date();
                        date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', date);
                    }
                }]

            },
            value: '',
        };
    },
    methods: {
        onDatePick(value) {
            eventBus.$emit('date-selected', value)
        }
    }
};
</script>