<template>
    <div style="width:100vw">
        <div autoresize ref="chart" style="height: 400px"></div>
    </div>
</template>
  
<script>
import axios from 'axios'
import { eventBus } from '@/main.js'

export default {
    data() {
        return {
            selectedDate: new Date().toISOString().slice(0, 10),
        }
    },

    mounted() {
        eventBus.$on('date-selected', value => {
            const date = new Date(value);
            const year = date.getFullYear();
            const month = date.getMonth() + 1;
            const day = date.getDate();
            const dateString = `${year}-${month < 10 ? '0' + month : month}-${day < 10 ? '0' + day : day}`;
            // console.log(dateString); // 输出：2023-05-17
            this.selectedDate = dateString
            const chartDom = this.$refs.chart
            this.$echarts.init(chartDom).dispose()
            this.getData()
        })
        eventBus.$on('seven-date-selected', value => {
            this.selectedDate = value
            const chartDom = this.$refs.chart
            this.$echarts.init(chartDom).dispose()
            this.getData()
        })
        this.getData()

    },
    methods: {
        async getData() {

            const res = await this.$http.get('http://localhost:8848/getAppRunningTimeOneDay/' + this.selectedDate)
            var arrx = []
            var arry = []
            var sum = 0
            for (var i = 0; i < res.data.length; i++) {
                arry.push(res.data[i]['app'])
                arrx.push((res.data[i]['totalRunningTime']))
                sum += arrx[i]
            }
            const total = Math.floor(sum / 3600) + '小时' + Math.floor((sum % 3600) / 60) + '分'
            const chartDom = this.$refs.chart
            const myChart = this.$echarts.init(chartDom)

            const option = {
                title: { text: this.selectedDate + "     App使用情况     " + total },
                xAxis: {
                    type: 'value',
                    max: 'dataMax',
                    name: 'time',
                    axisLabel: {
                        formatter: function (value) {
                            const hours = Math.floor(value / 3600); // 小时数
                            const minutes = Math.floor((value % 3600) / 60); // 分钟数
                            return hours + '小时' + minutes + '分';
                        }
                    },
                },
                yAxis: {
                    type: 'category',
                    data: arry,
                    inverse: true,
                    animationDuration: 500,
                    animationDurationUpdate: 500,
                    max: 7 // only the largest 3 bars will be displayed
                },
                series: [
                    {
                        realtimeSort: true,
                        type: 'bar',
                        data: arrx,
                        label: {
                            show: true,
                            position: 'right',
                            valueAnimation: true,
                            formatter: function (arrx) {
                                const hours = Math.floor(arrx.value / 3600); // 小时数
                                const minutes = Math.floor((arrx.value % 3600) / 60); // 分钟数
                                if (hours > 0 && minutes == 0) {
                                    return hours + '小时整';
                                }
                                else if (hours > 0 && minutes > 0) {
                                    return hours + '小时' + minutes + '分';
                                }
                                else if (minutes > 0) {
                                    return minutes + '分';
                                }
                                return arrx.value + '秒'
                            }
                        },
                    }
                ],
                legend: {
                    show: true
                },
                animationDuration: 500,
                animationDurationUpdate: 0,
                animationEasing: 'linear',
                animationEasingUpdate: 'linear'
            }
            myChart.setOption(option)

            setInterval(() => {
                axios.get('http://localhost:8848/getAppRunningTimeOneDay/' + this.selectedDate).then((r) => {
                    var x = []
                    var y = []
                    var sum = 0
                    for (var i = 0; i < r.data.length; i++) {
                        x.push(r.data[i]['app'])
                        y.push((r.data[i]['totalRunningTime']))
                        sum += y[i]
                    }
                    const total = Math.floor(sum / 3600) + '小时' + Math.floor((sum % 3600) / 60) + '分'
                    myChart.setOption({
                        title: { text: "当天App使用情况     " + total },
                        yAxis: {
                            data: x
                        },
                        series: [
                            {
                                type: 'bar',
                                data: y,
                            }
                        ]
                    })
                    // console.log(r)
                })

            }, 60 * 1000)
        },
    }
}
</script>