{{- define "cloud-gateway.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "cloud-gateway.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{/* Common labels */}}
{{- define "cloud-gateway.labels" -}}
app: {{ include "cloud-gateway.name" . }}
environment: {{ .Values.environment }}
app.kubernetes.io/name: {{ include "cloud-gateway.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/* Selector labels */}}
{{- define "cloud-gateway.selectorLabels" -}}
app: {{ include "cloud-gateway.name" . }}
{{- end }}
