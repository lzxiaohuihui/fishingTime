<template>
    <div style="width:100vw">
        <div autoresize ref="chart" style="height: 400px"></div>
    </div>
</template>
  
<script>
import { eventBus } from '@/main.js'
export default {
    data() {
        return {
            selectedDate: new Date().toISOString().slice(0, 10)
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
            this.selectedDate = dateString;
            const chartDom = this.$refs.chart
            this.$echarts.init(chartDom).dispose()
            this.getData();
        })
        this.getData();
    },
    methods: {
        async getData() {
            const res = await this.$http.get('http://localhost:8888/getSevenTimes/' + this.selectedDate)
            console.log(res.data)
            var arrx = []
            var arry = []
            var sum = 0
            for (var i = 0; i < res.data.length; i++) {
                arrx.push(res.data[i]['timeStamp'])
                arry.push(res.data[i]['totalRunningTime'] / 60)
                sum += res.data[i]['totalRunningTime'] / 60
            }
            var hours = Math.floor(sum / 60);
            var minutes = (sum % 60).toFixed(0);
            const total = hours + "小时" + minutes + "分"
            var data = {
                'categories': arrx,
                'values': arry,
                'total': total
            }
            this.renderChart(data)
        },
        renderChart(data) {
            const chartDom = this.$refs.chart
            const myChart = this.$echarts.init(chartDom)
            const option = {
                title: { text: "一周使用情况      " + data.total },
                tooltip: {
                    trigger: "axis",
                    axisPointer: {
                        type: "shadow"
                    },
                    formatter: function (params) {
                        var value = params[0].value;
                        if (value == 0) return ''
                        var hours = Math.floor(value / 60);
                        var minutes = (value % 60).toFixed(0);
                        return hours + "小时" + minutes + "分";
                    }
                },
                xAxis: {
                    type: 'category',
                    data: data.categories
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: function (value) {
                            var hours = Math.floor(value / 60);
                            if (value == 0) return ''
                            var minutes = value % 60;
                            return hours + "小时" + minutes + "分";
                        }
                    },

                },
                series: [{
                    data: data.values,
                    type: 'bar',
                    label: {
                        show: true,
                        position: 'top',
                        valueAnimation: true,
                        formatter: function (params) {
                            var value = params.value
                            if (value == 0) return ''
                            var hours = Math.floor(value / 60);
                            var minutes = (value % 60).toFixed(0);
                            return hours + "小时" + minutes + "分";
                        }
                    },
                }]
            }
            myChart.setOption(option)
            myChart.on('click', function (params) {
                eventBus.$emit('seven-date-selected', params.name)
            });
        }
    }
}
</script>