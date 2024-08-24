<template>
  <div id="app">
    <el-row :gutter="10" class="search">

      <el-col :offset="2" :span="4">
        <el-input v-model="uname" placeholder="请输入"></el-input>
      </el-col>

      <el-col :offset="2" :span="4">
        <el-button @click="query()" type="primary">查询</el-button>
        <el-button type="primary" @click="AddVisible = true">添加</el-button>
      </el-col>


      <el-col :offset="2" :span="20">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="uid" label="编号"></el-table-column>
          <el-table-column prop="uname" label="姓名"></el-table-column>
          <el-table-column prop="ubrith" label="出生日期"></el-table-column>
          <el-table-column prop="uaddress" label="地址"></el-table-column>
          <el-table-column prop="uemail" label="邮箱"></el-table-column>
          <el-table-column prop="uphone" label="电话号码"></el-table-column>
          <el-table-column prop="urole" label="角色"></el-table-column>
          <el-table-column prop="ucreated" label="创建时间"></el-table-column>
          <el-table-column prop="uupdated" label="更新时间"></el-table-column>
          <el-table-column prop="ustatus" label="状态"></el-table-column>

          <!-- 操作  自定义列 -->
          <el-table-column label="操作" width="200">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

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

    <el-dialog title="添加用户信息" :visible.sync="AddVisible" width="40%">
      <el-form :model="AddFormData">
        <el-form-item label="编号">
          <el-input v-model="AddFormData.uid" placeholder="请输入" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="AddFormData.uname" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label=" 地址">
          <el-input v-model="AddFormData.uaddress" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker v-model="AddFormData.ubrith" type="date" placeholder="选择日期" format="yyyy-MM-dd"
            value-format="yyyy-MM-dd'T'HH:mm:ss">
          </el-date-picker>

        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="AddFormData.uemail" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model="AddFormData.uphone" placeholder="请输入" type="tel"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-radio v-model="AddFormData.urole" label="运营团队成员">运营团队成员</el-radio>
          <el-radio v-model="AddFormData.urole" label="租户管理员">租户管理员</el-radio>
          <el-radio v-model="AddFormData.urole" label="普通用户">普通用户</el-radio>
        </el-form-item>
        <el-form-item label="是否激活">
          <el-radio v-model="AddFormData.ustatus" label="激活">激活</el-radio>
          <el-radio v-model="AddFormData.ustatus" label="停用">停用</el-radio>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="AddVisible = false">取消</el-button>
        <el-button type="primary" @click="add()">保存</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改学生信息" :visible.sync="dialogVisible" width="40%">
      <el-form :model="editFormData">
        <el-form-item label="学生编号">
          <el-input v-model="editFormData.uid" placeholder="请输入" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="editFormData.uname" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label=" 地址">
          <el-input v-model="editFormData.uaddress" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker v-model="editFormData.ubrith" type="date" placeholder="选择日期" format="yyyy-MM-dd"
            value-format="yyyy-MM-dd'T'HH:mm:ss">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="editFormData.uemail" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model="editFormData.uphone" placeholder="请输入" type="tel"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-radio v-model="editFormData.urole" label="运营团队成员">运营团队成员</el-radio>
          <el-radio v-model="editFormData.urole" label="租户管理员">租户管理员</el-radio>
          <el-radio v-model="editFormData.urole" label="普通用户">普通用户</el-radio>
        </el-form-item>
        <el-form-item label="是否激活">
          <el-radio v-model="editFormData.ustatus" label="激活">激活</el-radio>
          <el-radio v-model="editFormData.ustatus" label="停用">停用</el-radio>
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
// import { MessageBox } from 'element-ui';
// import { get } from 'core-js/core/dict';

export default {
  name: 'App',
  data() {
    return {
      dialogVisible: false,
      AddVisible: false,

      AddFormData: {
        uid: '',
        uname: '',
        ubrith: '',
        uaddress: '',
        uemail: '',
        uphone: '',
        urole: '',
        ucreated: '',
        uupdated: '',
        ustatus: '',
      },

      editFormData: {
        uid: '',
        uname: '',
        ubrith: '',
        uaddress: '',
        uemail: '',
        uphone: '',
        urole: '',
        ucreated: '',
        uupdated: '',
        ustatus: '',
      },

      tableData: [],
      uname: '',
      //editIndex: -1
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
      axios.get('http://localhost:8080/users/getByName', {
        params: {
          name: this.uname  
        }
      }).then(res => {
        console.log(res.data);
        this.tableData = res.data;
      });
    },


    handleEdit(index, row) {
      this.editIndex = index;
      console.log(index, row);
      this.editFormData = row;
      this.dialogVisible = true;
    },
    saveEdit() {
      axios.post(`http://localhost:8080/users/update/${this.editFormData.uid}`, this.editFormData)
        .then(() => {
          this.tableData.splice(this.tableData.findIndex(item => item.uid === this.editFormData.uid), 1, { ...this.editFormData });
          this.dialogVisible = false;
        })
        .catch(error => {
          console.error("更新失败：", error);
        });
    },
    add() {
      axios.post(`http://localhost:8080/users/add`, this.AddFormData)
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
        axios.get('http://localhost:8080/users/delete/' + row.uid).then(res => {
          console.log(res.data);
          this.getData()
        });
      })
    },
    getData() {
      axios.get('http://localhost:8080/users/getByPage', {
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
