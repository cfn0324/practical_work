<template>
  <div id="app">
    <el-row :gutter="10" class="search">

      <el-col :offset="2" :span="4" >
        <el-input v-model="projectname" placeholder="请输入"></el-input>
      </el-col>

      <el-col :offset="2" :span="4">
        <el-button @click="query()" type="primary">查询</el-button>
        <el-button type="primary" @click="AddVisible = true">添加</el-button>
      </el-col>


      <el-col :offset="2" :span="20">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="pid" label="编号"></el-table-column>
          <el-table-column prop="projectname" label="项目名"></el-table-column>
          <el-table-column prop="detal" label="具体细节"></el-table-column>

          <!-- 操作  自定义列 -->
          <el-table-column label="操作" width="300">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleView(scope.$index, scope.row)">查看</el-button>
              <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
              <el-button size="mini" type="danger"  @click="handleDelete(scope.$index, scope.row)">删除</el-button> 
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    
    <el-dialog title="查看信息" :visible.sync="WactchVisible" width="60%">
      <el-table :data="ftableData" style="width: 90%">
    <el-table-column prop="pusername" label="用户名"></el-table-column>
    <el-table-column prop="projectname" label="项目名"></el-table-column>
  </el-table>
  <span slot="footer" class="dialog-footer">
    <el-button type="danger" @click="WactchVisible = false">退出</el-button>
  </span>
</el-dialog>

    <!-- 分页 -->
    <el-row :gutter="20">
      <el-col :offset="2" :span="20">
        <div class="block">
          <!-- <span class="demonstration">完整功能</span> -->
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
            :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </div>
      </el-col>
    </el-row>

    <el-dialog title="添加信息" :visible.sync="AddVisible" width="40%">
      <el-form :model="AddFormData">
          <el-form-item label="编号">
              <el-input v-model="AddFormData.pid" placeholder="请输入" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="项目名">
              <el-input v-model="AddFormData.projectname" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label=" 具体细节">
              <el-input v-model="AddFormData.detal" placeholder="请输入"></el-input>
            </el-form-item>
          </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="AddVisible = false">取消</el-button>
        <el-button type="primary" @click="add()">保存</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改信息" :visible.sync="dialogVisible" width="40%">
      <el-form :model="editFormData">
        <el-form-item label="编号">
          <el-input v-model="editFormData.pid" placeholder="请输入" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="项目名">
          <el-input v-model="editFormData.projectname" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label=" 具体细节">
          <el-input v-model="editFormData.detal" placeholder="请输入"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import { MessageBox } from 'element-ui';
// import { get } from 'core-js/core/dict';

export default {
  name: 'App',
  data() {
    return {
      dialogVisible: false,
      AddVisible: false,
      WactchVisible:false,

      AddFormData: {
        pid: '',
        projectname: '',
        detal: '',
      },

      editFormData: {
        pid: '',
        projectname: '',
        detal: '',
      },
      ftableData: [],
      tableData: [],
      projectname: '',

      //分页属性，当前页
      pageNum: 1,
      //分页属性，每页条数
      pageSize: 5,
      //分页属性，总条数
      total: 0,
      //

    }
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
  axios.get('http://localhost:8080/project/getByName', {
    params: {
      projectname: this.projectname  
    }
  }).then(res => {
    console.log(res.data);
    this.tableData = res.data;
  });
},
handleView(index,row) {
    this.editIndex = index;
      console.log(index, row);
      this.projectname = row.projectname;
    this.fetchDataById(row.projectname);  // 获取对应id的数据
    this.WactchVisible = true;  // 打开对话框
  },

  fetchDataById(projectname) {
    console.log(projectname);
    axios.get('http://localhost:8080/projectuser/getBypName', {
  params: {
      projectname: this.projectname 
  }
}).then(res => {
  console.log(res.data);
  this.ftableData = res.data;
});
},
    handleEdit(index, row) {
      this.editIndex = index;
      console.log(index, row);
      this.editFormData = row;
      this.dialogVisible = true;
    },
    saveEdit() {
      axios.post(`http://localhost:8080/project/update/${this.editFormData.pid}`, this.editFormData)
        .then(() => {
          this.tableData.splice(this.tableData.findIndex(item => item.pid === this.editFormData.pid), 1, { ...this.editFormData });
          this.dialogVisible = false;
        })
        .catch(error => {
          console.error("更新失败：", error);
        });
    },
    add() {
      axios.post(`http://localhost:8080/project/add`, this.AddFormData)
        .then(response => {
          console.log(response);
          this.AddVisible = false;
          this.getData();
        })
        .catch(error => {
          console.error("更新失败：", error);
        });

    },
    handleDelete(index, row) {
      MessageBox.confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.get('http://localhost:8080/project/delete/' + row.pid).then(res => {
          console.log(res.data);
          this.getData()
        });
      })
    },
    getData() {
      axios.get('http://localhost:8080/project/getByPage',{
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
