<template>
  <div class="charts-container">
    <div id="ageChart" class="chart"></div>
    <div id="locationChart" class="chart"></div>
    <div id="statusChart" class="chart"></div>
    <div id="creationTimeChart" class="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import axios from 'axios';

export default {
  data() {
    return {
      ageGroups: ['0-20', '21-30', '31-40', '41-50', '51+'],
      ageGroupCounts: [0, 0, 0, 0, 0],
      userLocations: [],
      userStatusCounts: { active: 0, inactive: 0 },
      creationTimes: []
    };
  },
  methods: {
    fetchData() {
      axios.get('http://localhost:8080/users/getall')
        .then(response => {
          const users = response.data;
          const locationCounts = {};

          users.forEach(user => {
            const birthDate = new Date(user.ubrith);
            const creationDate = new Date(user.ucreated);

            // 检查日期对象是否有效
            if (!isNaN(creationDate)) {
              this.creationTimes.push(creationDate);
            } else {
              console.warn(`Invalid creation date for user: ${user.creationTime}`);
            }

            const age = new Date().getFullYear() - birthDate.getFullYear();
            if (age <= 20) {
              this.ageGroupCounts[0]++;
            } else if (age <= 30) {
              this.ageGroupCounts[1]++;
            } else if (age <= 40) {
              this.ageGroupCounts[2]++;
            } else if (age <= 50) {
              this.ageGroupCounts[3]++;
            } else {
              this.ageGroupCounts[4]++;
            }

            const location = user.uaddress.split(' ')[0];
            if (locationCounts[location]) {
              locationCounts[location]++;
            } else {
              locationCounts[location] = 1;
            }

            if (user.ustatus === '激活') {
              this.userStatusCounts.active++;
            } else {
              this.userStatusCounts.inactive++;
            }
          });

          this.userLocations = Object.keys(locationCounts).map(location => ({
            name: location,
            value: locationCounts[location]
          }));

          this.renderAgeChart();
          this.renderLocationChart();
          this.renderStatusChart();
          this.renderCreationTimeChart();
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
    },
    renderAgeChart() {
      var ageChart = echarts.init(document.getElementById('ageChart'));
      ageChart.setOption({
        title: {
          text: '用户年龄分布',
          left: 'center',
          textStyle: {
            color: '#333',
            fontSize: 18,
            fontWeight: 'bold'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: this.ageGroups,
          axisLine: {
            lineStyle: {
              color: '#333'
            }
          },
          axisLabel: {
            color: '#333'
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#333'
            }
          },
          axisLabel: {
            color: '#333'
          }
        },
        series: [{
          name: '用户数量',
          type: 'bar',
          data: this.ageGroupCounts,
          itemStyle: {
            color: '#73C0DE'
          },
          barWidth: '60%',
          label: {
            show: true,
            position: 'top',
            color: '#333'
          }
        }]
      });
    },
    renderLocationChart() {
      var locationChart = echarts.init(document.getElementById('locationChart'));
      axios.get('https://geo.datav.aliyun.com/areas_v2/bound/100000_full.json')
        .then(geoJson => {
          echarts.registerMap('china', geoJson.data);
          locationChart.setOption({
            title: {
              text: '用户地点分布',
              left: 'center',
              textStyle: {
                color: '#333',
                fontSize: 18,
                fontWeight: 'bold'
              }
            },
            tooltip: {
              trigger: 'item'
            },
            visualMap: {
              min: 0,
              max: Math.max(...this.userLocations.map(loc => loc.value)),
              left: 'left',
              top: 'bottom',
              text: ['高', '低'],
              calculable: true,
              inRange: {
                color: ['#E0FFFF', '#006EDD']
              }
            },
            series: [{
              name: '用户数量',
              type: 'map',
              mapType: 'china',
              roam: true,
              label: {
                show: true,
                color: '#000'
              },
              itemStyle: {
                areaColor: '#E0FFFF',
                borderColor: '#111'
              },
              emphasis: {
                label: {
                  show: true,
                  color: '#fff'
                },
                itemStyle: {
                  areaColor: '#006EDD'
                }
              },
              data: this.userLocations
            }]
          });
        })
        .catch(error => {
          console.error('Error fetching geoJson:', error);
        });
    },
    renderStatusChart() {
      var statusChart = echarts.init(document.getElementById('statusChart'));
      statusChart.setOption({
        title: {
          text: '用户状态分布',
          left: 'center',
          textStyle: {
            color: '#333',
            fontSize: 18,
            fontWeight: 'bold'
          }
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          textStyle: {
            color: '#333'
          }
        },
        series: [
          {
            name: '用户状态',
            type: 'pie',
            radius: '50%',
            data: [
              { value: this.userStatusCounts.active, name: '激活' },
              { value: this.userStatusCounts.inactive, name: '停用' }
            ],
            itemStyle: {
              color: function(params) {
                var colorList = ['#73C0DE', '#FF9F7F'];
                return colorList[params.dataIndex];
              }
            },
            label: {
              show: true,
              formatter: '{b}: {d}%',
              color: '#333'
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      });
    },
    renderCreationTimeChart() {
      const creationTimeChart = echarts.init(document.getElementById('creationTimeChart'));

      // 按年份和月份分组
      const creationTimeByMonth = this.creationTimes.reduce((acc, date) => {
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const formattedDate = `${year}-${month}`;
        if (!acc[formattedDate]) {
          acc[formattedDate] = 0;
        }
        acc[formattedDate]++;
        return acc;
      }, {});

      const months = Object.keys(creationTimeByMonth).sort();
      const counts = months.map(month => creationTimeByMonth[month]);

      console.log('Months:', months);
      console.log('Counts:', counts);

      const option = {
        title: {
          text: '用户新增折线图',
          left: 'center',
          textStyle: {
            color: '#333',
            fontSize: 18,
            fontWeight: 'bold'
          }
        },
        xAxis: {
          type: 'category',
          data: months,
          axisLine: {
            lineStyle: {
              color: '#333'
            }
          },
          axisLabel: {
            color: '#333'
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#333'
            }
          },
          axisLabel: {
            color: '#333'
          }
        },
        series: [{
          data: counts,
          type: 'line',
          itemStyle: {
            color: '#73C0DE'
          },
          label: {
            show: true,
            position: 'top',
            color: '#333'
          }
        }]
      };
      creationTimeChart.setOption(option);
    }
  },
  mounted() {
    this.fetchData();
  }
};
</script>

<style scoped>
.charts-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  align-items: center;
}

.chart {
  flex: 1 1 45%;
  margin: 20px;
  min-width: 300px;
  height: 400px;
}
</style>