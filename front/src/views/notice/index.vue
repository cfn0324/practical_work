<template>
    <div id="app">
      <el-row :gutter="20" class="search">
        <el-col :offset="2" :span="4">
          <el-input v-model="name" placeholder="请输入查询内容"></el-input>
        </el-col>
        <el-col :span="4">
          <el-button @click="query()" type="primary">查询</el-button>
        </el-col>
        <el-col :span="4">
          <el-button size="normal" type="success" @click="addVisile = true">添加</el-button>
        </el-col>
      </el-row>
  
      <!-- 建立的前端表单字段 -->
      <el-row>
        <el-col :offset="2" :span="20">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="id" label="公告序列"></el-table-column>
            <el-table-column prop="name" label="公告名称"></el-table-column>
            <el-table-column prop="content" label="公告内容"></el-table-column>
            <el-table-column prop="time" label="公告时间"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button size="normal" type="text" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                &nbsp;
                <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
  
      <!-- 添加弹出框 -->
      <el-dialog title="公告信息发布" :visible.sync="addVisile" width="30%">
        <!-- 添加表单 -->
        <el-form :model="addform" label-position="top">
          <!-- <el-form-item label="所属部门">
            <el-select v-model="addform.department" placeholder="请选择所属部门">
              <el-option label="研发部" value="研发部"></el-option>
          
            </el-select> 
          </el-form-item>-->
          <el-form-item label="公告序列">
            <el-input v-model="addform.id" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="公告名称">
            <el-input v-model="addform.name" placeholder="请输入公告名称"></el-input>
          </el-form-item>
          <el-form-item label="公告内容">
            <el-input type="textarea" v-model="addform.content" placeholder="请输入公告内容"></el-input>
          </el-form-item>
          <el-form-item label="公告时间">
            <el-input type="time" v-model="addform.time"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addVisile = false">重置</el-button>
          <el-button type="primary" @click="add()">立即发布</el-button>
        </div>
      </el-dialog>
  
      <!-- 编辑弹出框 -->
      <el-dialog title="更改公告信息" :visible.sync="dialogFormVisible" width="30%">
        <!-- 修改表单 -->
        <el-form :model="form" label-position="top">
          <el-form-item label="公告序列">
            <el-input v-model="form.id" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="公告名称">
            <el-input v-model="form.name" placeholder="请输入公告名称"></el-input>
          </el-form-item>
          <el-form-item label="公告内容">
            <el-input type="textarea" v-model="form.content" placeholder="请输入修改内容"></el-input>
          </el-form-item>
          <el-form-item label="公告时间">
            <el-input type="time" v-model="form.time"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="update()">确定</el-button>
        </div>
      </el-dialog>
  
      <!-- 分页功能 -->
      <el-row>
        <el-col :offset="2" :span="20">
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
  import { MessageBox } from 'element-ui';
  
  export default {
    name: 'App',
    data() {
      return {
        dialogFormVisible: false,
        addVisile: false,
        tableData: [],
        name: '',
  
  
        // 页码
        pageNum: 1,
        // 初始每页显示条数
        pageSize: 5,
        // 总条数
        total: 0,
  
        form: {
          id: '',
          name: '',
          content: '',
          time: ''
        },
  
        addform: {
          id: null,
          name: '',
          content: '',
          time: ''
        },
        formLabelWidth: '120px'
      };
    },
    methods: {
      handleCurrentChange(val) {
        this.pageNum = val;
        this.getData();
      },
      handleSizeChange(val) {
        this.pageSize = val;
        this.getData();
      },
  
      // 添加
      add() {
        console.log(this.addform)
        axios.post('http://127.0.0.1:8080/notice/add', this.addform)
          .then(Response => {
            console.log(Response);
            this.getData();
          }).catch(error => {
            console.log(error);
          });
        this.addVisile = false;
       
      },
   // 修改
      update() {
        axios.post('http://127.0.0.1:8080/notice/update', this.form)
          .then(Response => {
            console.log(Response);
          }).catch(error => {
            console.log(error);
          });
        this.dialogFormVisible = false;
        this.getData();
      },
  
      // 查询
      query() {
        axios.get('http://127.0.0.1:8080/notice/getNotice', {
          params: {
            name: this.name
          }
        }).then(res => {
          console.log(res.data);
          this.tableData = res.data;
        });
      },
  
      // 编辑
      handleEdit(index, row) {
        this.dialogFormVisible = true;
        this.form = row;
      },
  
      // 删除
      handleDelete(index, row) {
        MessageBox.confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          axios.get('http://localhost:8080/notice/delNoId/' + row.id).then(res => {
              console.log(res.data);
              this.getData();
            })
        })
      },
  
      // 获取数据
      getData() {
        axios.get('http://localhost:8080/notice/getByPage', {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize
          }
        }).then(res => {
          this.tableData = res.data.records;
          this.total = res.data.total;
          this.pageNum = res.data.current;
          this.pageSize = res.data.size;
        })
      }
    },
    mounted() {
      this.getData();
    }
  };
  </script>
  
  <style>
  .search {
    margin-bottom: 10px;
  }
  
  .dialog-footer {
    text-align: right;
  }
  </style>
  