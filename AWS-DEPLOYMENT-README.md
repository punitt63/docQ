# 🚀 DocQ AWS Deployment Guide

This guide will help you deploy your DocQ Docker application to AWS using modern cloud services.

## 📋 Prerequisites

Before starting, ensure you have:

1. **AWS Account** - [Create one here](https://aws.amazon.com)
2. **AWS CLI** - Install with `brew install awscli`
3. **Terraform** - Install with `brew install terraform`
4. **Docker** - Install with `brew install docker`

## 🏗️ Architecture Overview

Your application will be deployed using:

- **ECS Fargate**: Serverless container hosting
- **RDS PostgreSQL**: Managed database
- **ECR**: Container image registry
- **ALB**: Application Load Balancer
- **VPC**: Isolated network environment
- **CloudWatch**: Monitoring and logging

## 🚀 Quick Start

### Step 1: Configure AWS Credentials

```bash
# Configure your AWS credentials
aws configure

# You'll need:
# - AWS Access Key ID
# - AWS Secret Access Key
# - Default region: us-east-1
# - Default output format: json
```

### Step 2: Deploy Everything

```bash
# Run the deployment script
./deploy-to-aws.sh
```

That's it! The script will:
1. Create all AWS infrastructure
2. Build and push Docker images
3. Deploy services to ECS
4. Configure load balancer
5. Display access URLs

## 📊 What Gets Created

### Infrastructure Components

| Service | Purpose | Cost (Free Tier) |
|---------|---------|------------------|
| **VPC** | Network isolation | ✅ Free |
| **RDS PostgreSQL** | Database | ✅ Free (db.t3.micro) |
| **ECS Fargate** | Container hosting | ✅ Free (750 hours/month) |
| **ECR** | Image registry | ✅ Free (500MB/month) |
| **ALB** | Load balancer | ⚠️ ~$16/month |
| **NAT Gateway** | Internet access | ⚠️ ~$45/month |

### Estimated Monthly Cost
- **Free Tier**: $0 (first 12 months)
- **After Free Tier**: ~$61/month

## 🔧 Manual Deployment Steps

If you prefer to run commands manually:

### 1. Deploy Infrastructure

```bash
cd aws-infrastructure
terraform init
terraform plan
terraform apply
```

### 2. Build and Push Images

```bash
# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

# Build and push images
docker build -t keycloak:latest .
docker tag keycloak:latest <ecr-url>:latest
docker push <ecr-url>:latest

# Repeat for health-facility and patient services
```

### 3. Update ECS Services

```bash
aws ecs update-service --cluster docq-cluster --service docq-keycloak-service --force-new-deployment
aws ecs update-service --cluster docq-cluster --service docq-health-facility-service --force-new-deployment
aws ecs update-service --cluster docq-cluster --service docq-patient-service --force-new-deployment
```

## 🌐 Access Your Application

After deployment, you'll get URLs like:

- **Keycloak**: `http://docq-alb-123456789.us-east-1.elb.amazonaws.com:8080`
- **Health Facility**: `http://docq-alb-123456789.us-east-1.elb.amazonaws.com:9097`
- **Patient**: `http://docq-alb-123456789.us-east-1.elb.amazonaws.com:9098`

### Keycloak Admin Access
- **URL**: `http://your-alb-dns:8080`
- **Username**: `admin`
- **Password**: `admin123456`

## 📈 Monitoring

### AWS Console Links
- **ECS**: [View services](https://console.aws.amazon.com/ecs/home?region=us-east-1#/clusters/docq-cluster)
- **RDS**: [View database](https://console.aws.amazon.com/rds/home?region=us-east-1#databases:)
- **CloudWatch**: [View logs](https://console.aws.amazon.com/cloudwatch/home?region=us-east-1#logsV2:log-groups)
- **ECR**: [View images](https://console.aws.amazon.com/ecr/repositories?region=us-east-1)

### Health Checks
- **ECS Services**: Check task health in ECS console
- **Load Balancer**: Check target group health
- **Application**: Use `/health` endpoints

## 🔧 Configuration

### Environment Variables

The services are configured with these environment variables:

**Health Facility Service:**
- `SPRING_PROFILES_ACTIVE=aws`
- `KEYCLOAK_BASE_URL=http://keycloak:8080`
- `SPRING_DATASOURCE_URL=jdbc:postgresql://rds-endpoint:5432/health_facility`

**Patient Service:**
- `SPRING_PROFILES_ACTIVE=aws`
- `KEYCLOAK_BASE_URL=http://keycloak:8080`
- `HEALTH_FACILITY_BASE_URL=http://health-facility:9097`

### Database Setup

The RDS PostgreSQL instance is created with:
- **Database**: `health_facility`
- **Username**: `docq`
- **Password**: `docq123456`

## 🛠️ Troubleshooting

### Common Issues

1. **Services not starting**
   - Check ECS task logs in CloudWatch
   - Verify security group rules
   - Check environment variables

2. **Database connection issues**
   - Verify RDS security group allows ECS access
   - Check database endpoint and credentials

3. **Load balancer health checks failing**
   - Verify application health endpoints
   - Check security group rules
   - Ensure containers are listening on correct ports

### Useful Commands

```bash
# Check ECS service status
aws ecs describe-services --cluster docq-cluster --services docq-keycloak-service

# View CloudWatch logs
aws logs tail /ecs/docq/keycloak --follow

# Check RDS status
aws rds describe-db-instances --db-instance-identifier docq-postgres
```

## 🧹 Cleanup

To remove all AWS resources:

```bash
./cleanup-aws.sh
```

**Warning**: This will delete all resources and cannot be undone!

## 📚 Next Steps

1. **Set up custom domain** with Route 53
2. **Enable HTTPS** with ACM certificate
3. **Set up CI/CD** with GitHub Actions
4. **Add monitoring** with CloudWatch alarms
5. **Implement backup** strategy for RDS

## 💡 Tips for Beginners

1. **Start small**: Deploy one service at a time
2. **Monitor costs**: Use AWS Cost Explorer
3. **Use tags**: Tag resources for easy management
4. **Read logs**: CloudWatch logs are your friend
5. **Test locally**: Ensure Docker works before deploying

## 🆘 Getting Help

- **AWS Documentation**: [docs.aws.amazon.com](https://docs.aws.amazon.com)
- **ECS Guide**: [ECS Developer Guide](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/)
- **Terraform AWS Provider**: [registry.terraform.io](https://registry.terraform.io/providers/hashicorp/aws/latest)

---

**Happy Deploying! 🚀**
