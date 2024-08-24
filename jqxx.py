import pandas as pd
from sqlalchemy import create_engine
from prophet import Prophet
import matplotlib.pyplot as plt

# 创建数据库连接
engine = create_engine('mysql+pymysql://root:173205@localhost:3306/background')

# 读取users表数据
query = 'SELECT * FROM users'
users_data = pd.read_sql(query, engine)

# 提取创建日期并计算每月新增用户数
users_data['ucreated'] = pd.to_datetime(users_data['ucreated'], dayfirst=True)
users_data['month'] = users_data['ucreated'].dt.to_period('M')
growth_data = users_data.groupby('month').size().reset_index(name='y')

# 将period转换为datetime格式
growth_data['ds'] = growth_data['month'].dt.to_timestamp()
growth_data = growth_data[['ds', 'y']]

# 创建并训练Prophet模型
model = Prophet()
model.fit(growth_data)

# 进行预测
future = model.make_future_dataframe(periods=6, freq='M')  # 预测未来6个月
forecast = model.predict(future)

# 可视化结果
fig, ax = plt.subplots(figsize=(10, 6))
model.plot(forecast, ax=ax)

# 标注每个月的人数区间
for i, row in forecast.iterrows():
    if row['ds'] > growth_data['ds'].max():
        ax.text(row['ds'], row['yhat'], f"{int(row['yhat_lower'])}-{int(row['yhat_upper'])}", color='red', ha='center')

# 保存图片
output_path = 'D:\\实训\\vue-admin-template\\public\\forecast_plot.png'
plt.savefig(output_path)

import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import LabelEncoder

data=users_data

data['ubrith'] = pd.to_datetime(data['ubrith'], dayfirst=True)
data['ubrith_year'] = data['ubrith'].dt.year
data['ubrith_month'] = data['ubrith'].dt.month
data['ubrith_day'] = data['ubrith'].dt.day

data['uaddress'] = data['uaddress'].astype(str)
label_encoder = LabelEncoder()
data['uaddress_encoded'] = label_encoder.fit_transform(data['uaddress'])

data['ustatus'] = data['ustatus'].map({'激活': 1, '停用': 0})

X = data[['ubrith_year', 'ubrith_month', 'ubrith_day', 'uaddress_encoded']]
y = data['ustatus']

clf = RandomForestRegressor(n_estimators=100, random_state=42)
clf.fit(X, y)

y_pred = clf.predict(X)

pred=pd.array(y_pred)

#如果ustatus为1，pred要1-pred，如果ustatus为0，pred不变
for i in range(len(data)):
    if data['ustatus'][i]==1:
        pred[i]=1-pred[i]

pred_round=[round(i,3) for i in pred]

# 准备数据
data_to_insert = pd.DataFrame({
    'tqid': data['uid'],
    'tqname': data['uname'],
    'ustatus': data['ustatus'].map({1: '激活', 0: '停用'}),
    'gl': pred_round
})

# 读取现有数据
existing_data = pd.read_sql('SELECT * FROM tq', con=engine)

# 准备数据
data_to_insert = pd.DataFrame({
    'tqid': data['uid'],
    'tqname': data['uname'],
    'tqstatus': data['ustatus'].map({1: '激活', 0: '停用'}),
    'gl': pred_round
})

# 合并数据，更新现有记录
merged_data = pd.merge(existing_data, data_to_insert, on='tqid', how='outer', suffixes=('_old', ''))
merged_data = merged_data[['tqid', 'tqname', 'tqstatus', 'gl']]

# 插入或更新数据到 tq 表
merged_data.to_sql('tq', con=engine, if_exists='replace', index=False)