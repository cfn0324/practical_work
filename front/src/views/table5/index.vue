<template>
    <div id="app">
        <el-row :gutter="10" class="search">
  
            <el-col :offset="2" :span="4">
                <el-input v-model="name" placeholder="请输入"></el-input>
            </el-col>
  
            <el-col :offset="2" :span="4">
                <el-button @click="query()" type="primary">查询</el-button>
                <el-button type="primary" @click="AddVisible = true">添加</el-button>
            </el-col>
  
  
            <el-col :offset="2" :span="20">
                <el-table :data="tableData" style="width: 100%">
                    <el-table-column prop="id" label="编号"></el-table-column>
                    <el-table-column prop="name" label="用户名"></el-table-column>
                    <el-table-column prop="password" label="密码"></el-table-column>
                    <el-table-column prop="email" label="邮箱"></el-table-column>
                    <el-table-column prop="phone" label="电话号码"></el-table-column>
  
                    <!-- 操作  自定义列 -->
                    <el-table-column label="操作" width="200">
                        <template slot-scope="scope">
                            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                            <el-button size="mini" type="danger"
                                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-col>
        </el-row>
  
        <!-- 分页 -->
        <el-row :gutter="20">
            <el-col :offset="2" :span="20">
                <div class="block">
                    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                        :current-page="pageNum" :page-sizes="[5, 10, 15, 20]" :page-size="pageSize"
                        layout="total, sizes, prev, pager, next, jumper" :total="total">
                    </el-pagination>
                </div>
            </el-col>
        </el-row>
  
        <el-dialog title="添加授权用户" :visible.sync="AddVisible" width="40%">
            <el-form :model="AddFormData">
                <el-form-item label="编号">
                    <el-input v-model="AddFormData.id" placeholder="请输入" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="姓名">
                    <el-input v-model="AddFormData.name" placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label=" 密码">
                    <el-input v-model="AddFormData.password" placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="AddFormData.email" placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="电话号码">
                    <el-input v-model="AddFormData.phone" placeholder="请输入" type="tel"></el-input>
                </el-form-item>
            </el-form>
  
            <span slot="footer" class="dialog-footer">
                <el-button type="danger" @click="AddVisible = false">取消</el-button>
                <el-button type="primary" @click="add()">保存</el-button>
            </span>
        </el-dialog>
  
        <el-dialog title="修改授权信息" :visible.sync="dialogVisible" width="40%">
            <el-form :model="editFormData">
                <el-form-item label="编号">
                    <el-input v-model="editFormData.id" placeholder="请输入" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="姓名">
                    <el-input v-model="editFormData.name" placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label=" 密码">
                    <el-input v-model="editFormData.password" placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="editFormData.email" placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="电话号码">
                    <el-input v-model="editFormData.phone" placeholder="请输入" type="tel"></el-input>
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
  
  export default {
    name: 'App',
    data() {
        return {
            dialogVisible: false,
            AddVisible: false,
  
            AddFormData: {
                id: '',
                name: '',
                email: '',
                phone: '',
                password: '',
            },
  
            editFormData: {
                id: '',
                name: '',
                email: '',
                phone: '',
                password: '',
            },
  
            tableData: [],
            name: '',
            pageNum: 1,
            pageSize: 5,
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
            axios.get('http://localhost:8080/password/getByName', {
                params: {
                    name: this.name 
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
            axios.post(`http://localhost:8080/password/update/${this.editFormData.id}`, this.editFormData)
                .then(() => {
                    this.tableData.splice(this.tableData.findIndex(item => item.id === this.editFormData.id), 1, { ...this.editFormData });
                    this.dialogVisible = false;
                })
                .catch(error => {
                    console.error("更新失败：", error);
                });
        },
        add() {
            axios.post(`http://localhost:8080/password/add`, this.AddFormData)
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
                axios.get('http://localhost:8080/password/delete/' + row.id).then(res => {
                    console.log(res.data);
                    this.getData()
                });
            })
  
        },
        getData() {
            axios.get('http://localhost:8080/password/getByPage', {
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