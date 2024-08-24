<template>
  <div id="Index">
    <button @click="fetchUserData">开始预测</button>
    <img src="/forecast_plot.png" alt="Forecast Plot">
    <div v-if="userData">
      <pre>{{ userData }}</pre>
    </div>
  

    <el-row :gutter="10" class="search">


      <el-col :offset="2" :span="20">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="tqid" label="编号"></el-table-column>
          <el-table-column prop="tqname" label="姓名"></el-table-column>
          <el-table-column prop="tqstatus" label="状态"></el-table-column>
          <el-table-column prop="gl" label="用户 停用/激活 概率"></el-table-column>
        </el-table>
      </el-col>
    </el-row>

    
    <el-row :gutter="5">
      <el-col :offset="2" :span="5">
        <div class="block">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
            :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from 'axios';


export default {
  name: 'Index',
  data() {

    return {
   
     tableData: [],
      name: '',
      pageNum: 1,
      pageSize: 5,
      total: 0,

      AddFormData: {
        tqid: '',
        tqname: '',
        tqstatus: '',
        gl: '',
      },

    };
  },
  methods: {
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize = val;
      this.getData();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val;
      this.getData();
    },
    query() {
      axios.get('http://localhost:8080/tq/getByName', {
        params: {
          name: this.tqname  
        }
      }).then(res => {
        console.log(res.data);
        this.tableData = res.data;
      });
    },

    async fetchUserData() {
      try {
        const response = await axios.get('http://localhost:8080/users/getpy');
        this.userData = response.data;
        console.log(this.userData);
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    },
    getData() {
      axios.get('http://localhost:8080/tq/getByPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
      }).then(res => {
        console.log(res.data);
        this.tableData = res.data.records;
        this.total = res.data.total;
        this.pageNum = res.data.current;
        this.pageSize = res.data.size;
      });
    }
  },


  mounted() {
    this.getData();
  }
}
</script>

<style scoped></style>